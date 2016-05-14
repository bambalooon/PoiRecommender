package pl.edu.agh.eis.poirecommender.service;

import agh.Data;
import agh.ParseException;
import agh.Tree;
import agh.UId3;
import agh.UncertainEntropyEvaluator;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.dao.Visit;
import pl.edu.agh.eis.poirecommender.heartdroid.RulesProvider;
import pl.edu.agh.eis.poirecommender.visit.VisitService;

import java.io.IOException;
import java.util.List;

@Slf4j
public class RuleGeneratorService extends IntentService {
    private static final String RULE_GENERATOR_SERVICE_NAME = "PoiRecommender::RuleGenService";
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

    private String generateRules() throws ParseException, IOException {
        Data data = Data.parseUArffFromString(prepareArff());
        Tree result = UId3.growTree(data, new UncertainEntropyEvaluator());
        String generatedRules = result.toHML();
        String finalRules = addDefaultRuleAndRemoveCertaintyFromDecisions(generatedRules);
        log.info("GenRules: {}", finalRules);
        return finalRules;
    }

    private String prepareArff() throws IOException {
        List<Visit> visits = visitService.getVisits();
        String arffDeclaration = new String(ByteStreams.toByteArray(getResources().openRawResource(R.raw.declaration)));

        StringBuilder arffBuilder = new StringBuilder(arffDeclaration);
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

    /**
     * Thanks to adding additional rule to the end of HML file, it works like default rule
     * it's invoked only when no rule has met its conditions.
     * It's necessary to remove information about certainty from decisions,
     * because current version of HeaRTDroid does not support it.
     */
    private String addDefaultRuleAndRemoveCertaintyFromDecisions(String generatedRules) throws IOException {
        String defaultRule = new String(ByteStreams.toByteArray(getResources().openRawResource(R.raw.default_rule)));
        return generatedRules
                .replaceAll("</rule>\\s+</table>", "</rule>" + defaultRule + "</table>")
                .replaceAll("\\(#[\\d\\.]+\\)", "");
    }
}
