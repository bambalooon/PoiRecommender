package pl.edu.agh.eis.poirecommender.pois;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiManager {
    private static final Gson GSON_SERIALIZER = new Gson();
    private static final Type SERIALIZED_LIST_TYPE = new TypeToken<List<PoiAtDistance>>(){}.getType();
    private static final String POI_QUEUE_PREFERENCES = "POI_QUEUE_PREFERENCES";
    private static final String POI_QUEUE_PREFERENCE = "POI_QUEUE_PREFERENCE";
    private final SharedPreferences poiPreferences;

    public PoiManager(Context context) {
        this.poiPreferences = context.getSharedPreferences(POI_QUEUE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean setPoiList(List<PoiAtDistance> poiList) {
        return poiPreferences.edit()
                .putString(POI_QUEUE_PREFERENCE, GSON_SERIALIZER.toJson(poiList, SERIALIZED_LIST_TYPE))
                .commit();
    }

    public List<PoiAtDistance> getPoiList() {
        final String poiListJson = poiPreferences.getString(POI_QUEUE_PREFERENCE, null);
        Log.d("POI", poiListJson);
        return poiListJson == null
                ? new ArrayList<PoiAtDistance>()
                : GSON_SERIALIZER.<List<PoiAtDistance>>fromJson(poiListJson, SERIALIZED_LIST_TYPE);
    }
}
