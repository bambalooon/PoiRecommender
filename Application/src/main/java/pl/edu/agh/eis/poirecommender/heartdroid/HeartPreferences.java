package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.parser.hml.HMLParser;
import heart.xtt.XTTModel;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.utils.gson.GsonInterfaceAdapter;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class HeartPreferences {
    public static final int BASIC_POI_RECOMMENDER_CONFIG = R.raw.poi_recommender;
    private static final Gson GSON_SERIALIZER = new GsonBuilder()
            .registerTypeAdapter(Value.class, new GsonInterfaceAdapter<Value>())
            .registerTypeAdapter(ExpressionInterface.class, new GsonInterfaceAdapter<ExpressionInterface>())
            .create();
    private static final String HEART_PREFERENCES = "HEART_PREFERENCES";
    private static final String XTT_MODEL_PREFERENCE = "XTT_MODEL_PREFERENCE";
    private static final String TEMPORARY_XTT_MODEL_PREFERENCE = "TEMPORARY_XTT_MODEL_PREFERENCE";

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

    public void setXttModel(XTTModel xttModel) {
        heartPreferences.edit()
                .putString(XTT_MODEL_PREFERENCE, GSON_SERIALIZER.toJson(xttModel))
                .apply();
    }

    public XTTModel getTemporaryXttModel() {
        final String modelJson = heartPreferences.getString(TEMPORARY_XTT_MODEL_PREFERENCE, null);
        return GSON_SERIALIZER.fromJson(modelJson, XTTModel.class);
    }

    public void setTemporaryXttModel(XTTModel xttModel) {
        heartPreferences.edit()
                .putString(TEMPORARY_XTT_MODEL_PREFERENCE, GSON_SERIALIZER.toJson(xttModel))
                .apply();
    }

    public void cleanTemporaryXttModel() {
        heartPreferences.edit()
                .remove(TEMPORARY_XTT_MODEL_PREFERENCE)
                .apply();
    }

    private XTTModel initXttModel() {
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
