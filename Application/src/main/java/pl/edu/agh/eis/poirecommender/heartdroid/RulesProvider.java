package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import android.content.res.Resources;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.parser.hml.HMLParser;
import heart.xtt.XTTModel;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class RulesProvider {
    public static final int BASIC_POI_RECOMMENDER_CONFIG = R.raw.poi_recommender;

    private final Resources resources;

    public RulesProvider(Context context) {
        this.resources = context.getResources();
    }

    public XTTModel getXttModel() {
        final InputStream poiRecommenderStream = resources.openRawResource(BASIC_POI_RECOMMENDER_CONFIG);
        try {
            return HMLParser.parseHML(poiRecommenderStream);
        } catch (BuilderException | NotInTheDomainException | RangeFormatException e) {
            log.error("Could not load XttModel.", e);
            return null;
        } finally {
            try {
                poiRecommenderStream.close();
            } catch (IOException e) {
                log.error("IOException when closing XttModel stream.", e);
            }
        }
    }
}
