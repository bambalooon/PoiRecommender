package pl.edu.agh.eis.poirecommender.pois;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.utils.gson.GsonInterfaceAdapter;

public class PoiManager {
    private static final Gson GSON_SERIALIZER = new GsonBuilder()
            .registerTypeAdapter(Poi.class, new GsonInterfaceAdapter<Poi>())
            .create();
    private static final String POI_PREFERENCES = "POI_PREFERENCES";
    private static final String RECOMMENDED_POIS = "POI_STORAGE_PREFERENCE";
    private static final String FILTERED_RECOMMENDED_POIS = "FILTERED_RECOMMENDED_POIS";
    private final SharedPreferences poiPreferences;

    public PoiManager(Context context) {
        this.poiPreferences = context.getSharedPreferences(POI_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setRecommendedPois(PoiStorage poiStorage) {
        poiPreferences.edit()
                .putString(RECOMMENDED_POIS, GSON_SERIALIZER.toJson(poiStorage))
                .apply();
    }

    public PoiStorage getRecommendedPois() {
        final String poiStorageJson = poiPreferences.getString(RECOMMENDED_POIS, null);
        return GSON_SERIALIZER.fromJson(poiStorageJson, PoiStorage.class);
    }

    public void setFilteredRecommendedPois(PoiStorage poiStorage) {
        poiPreferences.edit()
                .putString(FILTERED_RECOMMENDED_POIS, GSON_SERIALIZER.toJson(poiStorage))
                .apply();
    }

    public PoiStorage getFilteredRecommendedPois() {
        final String poiStorageJson = poiPreferences.getString(FILTERED_RECOMMENDED_POIS, null);
        if (poiStorageJson == null) {
            return getRecommendedPois();
        }
        return GSON_SERIALIZER.fromJson(poiStorageJson, PoiStorage.class);
    }
}
