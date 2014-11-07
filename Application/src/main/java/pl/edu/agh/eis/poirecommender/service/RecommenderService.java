package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import heart.xtt.StateElement;
import pl.edu.agh.eis.poirecommender.aware.AwarePreferences;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.ActivityAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.InterestListAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WeatherAdapter;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;

/**
 * Created by Krzysztof Balon on 2014-10-30.
 */
public class RecommenderService extends IntentService {
    public static final String CONTEXT_REFRESH_ACTION = "CONTEXT_REFRESH_CONTEXT";
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private static final String TAG = RecommenderService.class.getName();
    private final Handler handler;
    private AwarePreferences awarePreferences;
    private InterestPreferences interestPreferences;

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
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if(CONTEXT_REFRESH_ACTION.equals(intent.getAction())) {
            Log.d(TAG, awarePreferences.areAllPreferencesSet() ? "All preferences set!" : "Not all preferences set...");
            Log.d(TAG, "\n" + awarePreferences.getActivity() + "\n" + awarePreferences.getWeather() + "\n" + awarePreferences.getLocation());
            Log.d(TAG, FluentIterable.from(interestPreferences.getInterests()).join(Joiner.on("; ")));
            if(awarePreferences.areAllPreferencesSet()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                intent.getAction(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            StateElement activityState = new ActivityAdapter(awarePreferences.getActivity()).getStateElement();
            StateElement weatherState = new WeatherAdapter(awarePreferences.getWeather()).getStateElement();
            StateElement interestsState = new InterestListAdapter(interestPreferences.getInterests()).getStateElement();
            Log.d(TAG, activityState.getAttributeName() + " : " + activityState.getValue() + " : " + activityState.getValue().getCertaintyFactor());
            Log.d(TAG, weatherState.getAttributeName() + ": " + weatherState.getValue() + " : " + weatherState.getValue().getCertaintyFactor());
            Log.d(TAG, interestsState.getAttributeName() + ": " + interestsState.getValue() + " : " + interestsState.getValue().getCertaintyFactor());
        }
    }
}
