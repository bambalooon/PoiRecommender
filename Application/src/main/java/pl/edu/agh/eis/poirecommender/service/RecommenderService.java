package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
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
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;

/**
 * Created by Krzysztof Balon on 2014-10-30.
 */
public class RecommenderService extends IntentService {
    public static final String CONTEXT_REFRESH_ACTION = "CONTEXT_REFRESH_CONTEXT";
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private static final String TAG = RecommenderService.class.getSimpleName();
    private final Handler handler;
    private AwarePreferences awarePreferences;
    private InterestPreferences interestPreferences;
    private HeartManager heartManager;

    public static void notifyRecommender(Context context) {
        Intent notificationIntent = new Intent(context, RecommenderService.class);
        notificationIntent.setAction(RecommenderService.CONTEXT_REFRESH_ACTION);
        context.startService(notificationIntent);
    }

    public RecommenderService() {
        super(RECOMMENDER_SERVICE_NAME);
        handler = new Handler(); //FIXME: to removal, debug tool
    }

    @Override
    public void onCreate() {
        super.onCreate();
        awarePreferences = new AwarePreferences(getApplicationContext());
        interestPreferences = new InterestPreferences(getApplicationContext());
        heartManager = new HeartManager(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if(CONTEXT_REFRESH_ACTION.equals(intent.getAction())) {
            Log.d(TAG, awarePreferences.areAllPreferencesSet() ? "All preferences set!" : "Not all preferences set...");
            Log.d(TAG, "\n" + awarePreferences.getActivity() + "\n" + awarePreferences.getWeather() + "\n" + awarePreferences.getLocation());
            Log.d(TAG, FluentIterable.from(interestPreferences.getInterests()).join(Joiner.on("; ")));
            final ImmutableList<WithStateElement> stateElements = ImmutableList.of(
                    new ActivityAdapter(awarePreferences.getActivity()),
                    new WeatherAdapter(awarePreferences.getWeather()),
                    new InterestListAdapter(interestPreferences.getInterests()));

            final PoiType recommendedPoiType = heartManager.inferencePreferredPoiType(stateElements)
                    .getPoiType();

            Location location = awarePreferences.getLocation();
            if(recommendedPoiType != null) {
                Log.d(TAG, "Recommendation poi type: " + recommendedPoiType.getText());
                if(location != null) {
                    String response = new OsmExecutor().execute(new OsmRequest(PoiTypeToConstraintMap.get(recommendedPoiType), location));
                    Log.d(TAG, response);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                recommendedPoiType.getText(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
