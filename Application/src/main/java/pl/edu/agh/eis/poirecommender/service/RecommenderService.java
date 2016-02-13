package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.storage.ContextStorage;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.plugin.google.activity_recognition.Google_AR_Provider;
import com.aware.plugin.openweather.Provider;
import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.aware.poirecommender.provider.PoiRecommenderContract;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.GenericContextPropertyNumericStateAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.GenericContextPropertySymbolicStateAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.TimeHourAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Date;

/**
 * Name: RecommenderService
 * Description: RecommenderService
 * Date: 2014-10-30
 * Created by BamBalooon
 */
@Slf4j
public class RecommenderService extends IntentService {
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private ContextStorage<GenericContextProperty> contextStorage;
    private LocationHolder locationHolder;
    private HeartManager heartManager;
    private PoiManager poiManager;

    public RecommenderService() {
        super(RECOMMENDER_SERVICE_NAME);
    }

    public static void notifyRecommender(android.content.Context context) {
        Intent notificationIntent = new Intent(context, RecommenderService.class);
        context.startService(notificationIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        android.content.Context appContext = getApplicationContext();
        contextStorage = new Context(appContext.getContentResolver(),
                new ContextPropertySerialization<>(GenericContextProperty.class));
        locationHolder = new AwareLocationHolder(contextStorage);
        heartManager = new HeartManager(appContext);
        poiManager = new PoiManager(appContext);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        debugInfo();

        final Location location = locationHolder.getLocation();
        if (location == null) {
            return;
        }

        final ImmutableList<WithStateElement> stateElements = ImmutableList.of(
                new GenericContextPropertySymbolicStateAdapter(
                        contextStorage.getContextProperty(PoiRecommenderContract.Contexts
                                .PLUGIN_GOOGLE_ACTIVITY_RECOGNITION_TIMESTAMP),
                        "activity",
                        Google_AR_Provider.Google_Activity_Recognition_Data.ACTIVITY_NAME),
                new GenericContextPropertyNumericStateAdapter(
                        contextStorage.getContextProperty(PoiRecommenderContract.Contexts.PLUGIN_OPENWEATHER_TIMESTAMP),
                        "windInMPS",
                        Provider.OpenWeather_Data.WIND_SPEED),
                new GenericContextPropertyNumericStateAdapter(
                        contextStorage.getContextProperty(PoiRecommenderContract.Contexts.PLUGIN_OPENWEATHER_TIMESTAMP),
                        "tempInC",
                        Provider.OpenWeather_Data.TEMPERATURE),
                new GenericContextPropertyNumericStateAdapter(
                        contextStorage.getContextProperty(PoiRecommenderContract.Contexts.PLUGIN_OPENWEATHER_TIMESTAMP),
                        "rainVal",
                        Provider.OpenWeather_Data.RAIN),
                new TimeHourAdapter(new Date()));

        final PoiType recommendedPoiType = heartManager.inferencePreferredPoiType(stateElements)
                .getPoiType();

        if (recommendedPoiType != null) {
            log.debug("Recommendation poi type: " + recommendedPoiType.getText());
            final OsmRequest osmRequest = new OsmJsonRequest(PoiTypeToConstraintMap.getConstraint(recommendedPoiType), location);
            OsmResponse osmResponse = new OsmExecutor().execute(osmRequest, getApplicationContext());
            if (osmResponse != null) {
                final PoiStorage poiStorage = PoiStorage.fromOsmResponse(osmResponse);
                this.poiManager.setPoiStorage(poiStorage);
            }
        }
    }

    private void debugInfo() {
        log.debug(contextStorage.getContextProperties().toString());
    }
}
