package pl.edu.agh.eis.poirecommender.service;

import agh.Data;
import agh.ParseException;
import agh.Tree;
import agh.UId3;
import agh.UncertainEntropyEvaluator;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.dao.Visit;
import pl.edu.agh.eis.poirecommender.heartdroid.RulesProvider;
import pl.edu.agh.eis.poirecommender.visit.VisitService;

import java.io.IOException;
import java.util.List;

@Slf4j
public class RuleGeneratorService extends IntentService {
    private static final String RULE_GENERATOR_SERVICE_NAME = "PoiRecommender::RuleGenService";
    private static final String ARFF_DECLARATION = "@relation poi.symbolic\n" +
            "\n" +
            "@attribute day_part {em, lm, ea, la, ee, le, n}\n" +
            "@attribute weekday {mon, tue, wed, thu, fri, sat, sun}\n" +
            "@attribute temperature {hot, mild, cool, cold}\n" +
            "@attribute windy {true, false}\n" +
            "@attribute rain {none, light, heavy}\n" +
            "@attribute poi {indoor-eating, outdoor-eating, indoor-sports, outdoor-sports, theatre-cinema, monuments, indoor-entertainment, outdoor-entertainment, museum, shopping-center}\n" +
            "\n" +
            "@data";
    private static final String DEFAULT_RULE = "<rule id=\"default_rule\">\n" +
            "                <condition>\n" +
            "                    <relation name=\"eq\">\n" +
            "                        <attref ref=\"rain\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"any\"/>\n" +
            "                        </set>\n" +
            "                    </relation>\n" +
            "                    <relation name=\"eq\">\n" +
            "                        <attref ref=\"temperature\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"any\"/>\n" +
            "                        </set>\n" +
            "                    </relation>\n" +
            "                    <relation name=\"eq\">\n" +
            "                        <attref ref=\"weekday\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"any\"/>\n" +
            "                        </set>\n" +
            "                    </relation>\n" +
            "                    <relation name=\"eq\">\n" +
            "                        <attref ref=\"day_part\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"any\"/>\n" +
            "                        </set>\n" +
            "                    </relation>\n" +
            "                    <relation name=\"eq\">\n" +
            "                        <attref ref=\"windy\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"any\"/>\n" +
            "                        </set>\n" +
            "                    </relation>\n" +
            "                </condition>\n" +
            "                <decision>\n" +
            "                    <trans>\n" +
            "                        <attref ref=\"poi\"/>\n" +
            "                        <set>\n" +
            "                            <value is=\"indoor-eating\"/>\n" +
            "                        </set>\n" +
            "                    </trans>\n" +
            "                </decision>\n" +
            "            </rule>";
    private VisitService visitService;
    private RulesProvider rulesProvider;

    public RuleGeneratorService() {
        super(RULE_GENERATOR_SERVICE_NAME);
    }

    public static void notify(Context context) {
        Intent notificationIntent = new Intent(context, RuleGeneratorService.class);
        context.startService(notificationIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context appContext = getApplicationContext();
        visitService = new VisitService(appContext);
        rulesProvider = new RulesProvider(appContext);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        try {
            rulesProvider.saveXttModel(generateRules());
            RecommenderService.notifyRecommender(getApplicationContext());
        } catch (ParseException | IOException e) {
            log.error("Error while generating rules", e);
        }
    }

    private String generateRules() throws ParseException {
        Data data = Data.parseUArffFromString(prepareArff());
        Tree result = UId3.growTree(data, new UncertainEntropyEvaluator());
        String generatedRules = result.toHML();
        String finalRules = addDefaultRuleAndRemoveCertaintyFromDecisions(generatedRules);
        log.info("GenRules: {}", finalRules);
        return finalRules;
    }

    private String prepareArff() {
        List<Visit> visits = visitService.getVisits();
        StringBuilder arffBuilder = new StringBuilder(ARFF_DECLARATION);
        for (Visit visit : visits) {
            arffBuilder.append("\n").append(visitToArffDataLine(visit));
        }
        return arffBuilder.toString();
    }

    private String visitToArffDataLine(Visit visit) {
        return visit.getDayPart() + ","
                + visit.getWeekDay() + ","
                + visit.getTemperature() + ","
                + visit.getWindy() + ","
                + visit.getRain() + ","
                + visit.getPoiType().getText();
    }

    private String addDefaultRuleAndRemoveCertaintyFromDecisions(String generatedRules) {
        return generatedRules
                .replaceAll("</rule>\\s+</table>", "</rule>" + DEFAULT_RULE + "</table>")
                .replaceAll("\\(#[\\d\\.]+\\)", "");
    }
}
