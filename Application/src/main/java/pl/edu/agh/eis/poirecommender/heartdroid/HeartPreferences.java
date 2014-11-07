package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.google.gson.Gson;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.parser.hml.HMLParser;
import heart.xtt.XTTModel;
import pl.edu.agh.eis.poirecommender.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Krzysztof Balon on 2014-11-07.
 */
public class HeartPreferences {
    public static final int BASIC_POI_RECOMMENDER_CONFIG = R.raw.poi_recommender;
    private static final Gson GSON_SERIALIZER = new Gson();
    private static final String HEART_PREFERENCES = "HEART_PREFERENCES";
    private static final String XTT_MODEL_PREFERENCE = "XTT_MODEL_PREFERENCE";

    private final SharedPreferences heartPreferences;
    private final Resources resources;

    public HeartPreferences(Context context) {
        this.heartPreferences = context.getSharedPreferences(HEART_PREFERENCES, Context.MODE_PRIVATE);
        this.resources = context.getResources();
    }

    public XTTModel getXttModel() {
        final String modelJson = heartPreferences.getString(XTT_MODEL_PREFERENCE, null);
        final XTTModel xttModel = GSON_SERIALIZER.fromJson(modelJson, XTTModel.class);
        if(xttModel == null) {
            return initXttModel();
        }
        return xttModel;
    }

    public boolean setXttModel(XTTModel xttModel) {
        return heartPreferences.edit()
                .putString(XTT_MODEL_PREFERENCE, GSON_SERIALIZER.toJson(xttModel))
                .commit();
    }

    private XTTModel initXttModel() {
        final InputStream poiRecommenderStream = resources.openRawResource(BASIC_POI_RECOMMENDER_CONFIG);
        try {
            return HMLParser.parseHML(poiRecommenderStream);
        } catch (BuilderException | NotInTheDomainException | RangeFormatException e) {
            return null;
        } finally {
            try {
                poiRecommenderStream.close();
            } catch (IOException e) {}
        }
    }
}
