package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import pl.edu.agh.eis.poirecommender.aware.AwarePreferences;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.*;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;
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
    public static final String CONTEXT_REFRESH_ACTION = "CONTEXT_REFRESH_CONTEXT";
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private static final String TAG = RecommenderService.class.getSimpleName();
    private AwarePreferences awarePreferences;
    private InterestPreferences interestPreferences;
    private HeartManager heartManager;
    private PoiManager poiManager;

    public static void notifyRecommender(Context context) {
        Intent notificationIntent = new Intent(context, RecommenderService.class);
        notificationIntent.setAction(RecommenderService.CONTEXT_REFRESH_ACTION);
        context.startService(notificationIntent);
    }

    public RecommenderService() {
        super(RECOMMENDER_SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        awarePreferences = new AwarePreferences(getApplicationContext());
        interestPreferences = new InterestPreferences(getApplicationContext());
        heartManager = new HeartManager(getApplicationContext());
        poiManager = new PoiManager(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if(CONTEXT_REFRESH_ACTION.equals(intent.getAction())) {
            debugInfo();
            final ImmutableList<WithStateElement> stateElements = ImmutableList.of(
                    new ActivityAdapter(awarePreferences.getActivity()),
                    new WeatherRainAdapter(awarePreferences.getWeather()),
                    new WeatherWindAdapter(awarePreferences.getWeather()),
                    new WeatherTemperatureAdapter(awarePreferences.getWeather()),
                    new TimeHourAdapter(new Date()),
                    new InterestListAdapter(interestPreferences.getInterestStorage().getInterests()));

            final PoiType recommendedPoiType = heartManager.inferencePreferredPoiType(stateElements)
                    .getPoiType();

            final Location location = awarePreferences.getLocation();

            if(recommendedPoiType != null && location != null) {
                Log.d(TAG, "Recommendation poi type: " + recommendedPoiType.getText());
                final OsmRequest osmRequest = new OsmJsonRequest(PoiTypeToConstraintMap.getConstraint(recommendedPoiType), location);
                OsmResponse osmResponse = new OsmExecutor().execute(osmRequest, getApplicationContext());
                if(osmResponse != null) {
                    final PoiStorage poiStorage = PoiStorage.fromOsmResponse(osmResponse);
                    this.poiManager.setPoiStorage(poiStorage);
                }
            }
        }
    }

    private void debugInfo() {
        Log.d(TAG, awarePreferences.areAllPreferencesSet() ? "All preferences set!" : "Not all preferences set...");
        Log.d(TAG, "\n" + awarePreferences.getActivity() + "\n" + awarePreferences.getWeather() + "\n" + awarePreferences.getLocation());
        Log.d(TAG, FluentIterable.from(interestPreferences.getInterestStorage().getInterests()).join(Joiner.on("; ")));
    }
}
