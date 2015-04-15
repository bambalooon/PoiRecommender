package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.storage.ContextStorage;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.plugin.google.activity_recognition.Google_AR_Provider;
import com.aware.plugin.openweather.Provider;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.*;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.interests.InterestStorage;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.OsmResponse;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;

import java.util.Date;

/**
 * Created by Krzysztof Balon on 2014-10-30.
 */
public class RecommenderService extends IntentService {
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private static final String TAG = RecommenderService.class.getSimpleName();
    private ContextStorage<GenericContextProperty> awareContextStorage;
    private AwareLocationHolder awareLocationHolder;
    private InterestStorage interestStorage;
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
        awareContextStorage = new Context(appContext.getContentResolver(),
                new ContextPropertySerialization<>(GenericContextProperty.class));
        awareLocationHolder = new AwareLocationHolder(awareContextStorage);
        interestStorage = new InterestStorage(appContext);
        heartManager = new HeartManager(appContext);
        poiManager = new PoiManager(appContext);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        debugInfo();
        final ImmutableList<WithStateElement> stateElements = ImmutableList.of(
                new GenericContextPropertySymbolicStateAdapter(
                        awareContextStorage.getContextProperty(Google_AR_Provider.AUTHORITY),
                        "activity",
                        Google_AR_Provider.Google_Activity_Recognition_Data.ACTIVITY_NAME),
                new GenericContextPropertyNumericStateAdapter(
                        awareContextStorage.getContextProperty(Provider.AUTHORITY),
                        "windInMPS",
                        Provider.OpenWeather_Data.WIND_SPEED),
                new GenericContextPropertyNumericStateAdapter(
                        awareContextStorage.getContextProperty(Provider.AUTHORITY),
                        "tempInC",
                        Provider.OpenWeather_Data.TEMPERATURE),
                new GenericContextPropertyNumericStateAdapter(
                        awareContextStorage.getContextProperty(Provider.AUTHORITY),
                        "rainVal",
                        Provider.OpenWeather_Data.RAIN),
                new TimeHourAdapter(new Date()),
                new InterestListAdapter(interestStorage.getInterestList()));

        final PoiType recommendedPoiType = heartManager.inferencePreferredPoiType(stateElements)
                .getPoiType();

        final Location location = awareLocationHolder.getLocation();

        if (recommendedPoiType != null && location != null) {
            Log.d(TAG, "Recommendation poi type: " + recommendedPoiType.getText());
            final OsmRequest osmRequest = new OsmJsonRequest(PoiTypeToConstraintMap.getConstraint(recommendedPoiType), location);
            OsmResponse osmResponse = new OsmExecutor().execute(osmRequest, getApplicationContext());
            if (osmResponse != null) {
                final PoiStorage poiStorage = PoiStorage.fromOsmResponse(osmResponse);
                this.poiManager.setPoiStorage(poiStorage);
            }
        }
    }

    private void debugInfo() {
        Log.d(TAG, awareContextStorage.getContextProperties().toString());
        Log.d(TAG, FluentIterable.from(interestStorage.getInterestList()).join(Joiner.on("; ")));
    }
}
