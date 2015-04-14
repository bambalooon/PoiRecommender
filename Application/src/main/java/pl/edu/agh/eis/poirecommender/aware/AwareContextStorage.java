package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import android.content.SharedPreferences;
import com.aware.context.property.GenericContextProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Krzysztof Balon on 2014-10-31.
 */
public class AwareContextStorage {
    private static final String AWARE_PREFERENCES = "AWARE_PREFERENCES";
    private static final Gson GSON_SERIALIZER = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .create();

    private final SharedPreferences awarePreferences;

    public AwareContextStorage(Context context) {
        this.awarePreferences = context.getSharedPreferences(AWARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public GenericContextProperty getContextProperty(String contextPropertyName) {
        String contextPropertyJson = awarePreferences.getString(contextPropertyName, null);
        return deserializeContextProperty(contextPropertyJson);
    }

    public void setContextProperty(GenericContextProperty contextProperty) {
        String contextPropertyJson = GSON_SERIALIZER.toJson(contextProperty);
        awarePreferences.edit()
                .putString(contextProperty.getId(), contextPropertyJson)
                .apply();
    }

    public List<GenericContextProperty> getContextProperties() {
        List<GenericContextProperty> contextProperties = new ArrayList<>();
        Map<String, ?> persistedContextProperties = awarePreferences.getAll();
        for (Object contextPropertyJson : persistedContextProperties.values()) {
            contextProperties.add(deserializeContextProperty((String) contextPropertyJson));
        }
        return contextProperties;
    }

    private GenericContextProperty deserializeContextProperty(String contextPropertyJson) {
        return GSON_SERIALIZER.fromJson(contextPropertyJson, GenericContextProperty.class);
    }

}
