package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import pl.edu.agh.eis.poirecommender.aware.AwarePreferences;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartManager;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.ActivityAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.InterestListAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WeatherAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.Element;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.OsmResponse;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.model.BasicPoi;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;

import java.util.List;

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
    private PoiManager poiManager;

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
        poiManager = new PoiManager(getApplicationContext());
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

            final Location location = awarePreferences.getLocation();
            if(recommendedPoiType != null) {
                Log.d(TAG, "Recommendation poi type: " + recommendedPoiType.getText());
                if(location != null) {
                    final OsmRequest osmRequest = new OsmJsonRequest(PoiTypeToConstraintMap.get(recommendedPoiType), location);
                    OsmResponse osmResponse = new OsmExecutor().execute(osmRequest, getApplicationContext());
                    if(osmResponse != null) {
                        final List<PoiAtDistance> poiList = FluentIterable.from(osmResponse.getElements())
                                .transform(new Function<Element, PoiAtDistance>() {
                                    @Override
                                    public PoiAtDistance apply(Element poiElement) {
                                        return new PoiAtDistance(BasicPoi.fromOsmElement(poiElement), location);
                                    }
                                }).filter(new Predicate<PoiAtDistance>() {
                                    @Override
                                    public boolean apply(PoiAtDistance poi) {
                                        final String poiName = poi.getName();
                                        return poiName != null && !poiName.trim().isEmpty();
                                    }
                                }).toList();
                        this.poiManager.setPoiList(poiList);
                        Log.d(TAG, "PoiList size: " + poiList.size());
                        Log.d(TAG, osmResponse.toString());
                    }
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
