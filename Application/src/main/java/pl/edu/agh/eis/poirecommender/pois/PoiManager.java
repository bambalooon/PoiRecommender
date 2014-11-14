package pl.edu.agh.eis.poirecommender.pois;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.edu.agh.eis.poirecommender.pois.gson.GsonPoiAdapter;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiManager {
    private static final Gson GSON_SERIALIZER = new GsonBuilder().registerTypeAdapter(Poi.class, new GsonPoiAdapter()).create();
    private static final String POI_PREFERENCES = "POI_PREFERENCES";
    private static final String POI_STORAGE_PREFERENCE = "POI_STORAGE_PREFERENCE";
    private final SharedPreferences poiPreferences;

    public PoiManager(Context context) {
        this.poiPreferences = context.getSharedPreferences(POI_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean setPoiStorage(PoiStorage poiStorage) {
        return poiPreferences.edit()
                .putString(POI_STORAGE_PREFERENCE, GSON_SERIALIZER.toJson(poiStorage))
                .commit();
    }

    public PoiStorage getPoiStorage() {
        final String poiStorageJson = poiPreferences.getString(POI_STORAGE_PREFERENCE, null);
        return GSON_SERIALIZER.fromJson(poiStorageJson, PoiStorage.class);
    }
}
