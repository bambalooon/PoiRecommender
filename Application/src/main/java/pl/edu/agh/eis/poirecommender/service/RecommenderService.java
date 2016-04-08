package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Intent;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.storage.ContextStorage;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.plugin.openweather.Provider;
import com.aware.poirecommender.provider.PoiRecommenderContract;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartRuleEngine;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.GenericContextPropertyNumericStateAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.TimeHourAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;

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
    private HeartRuleEngine heartRuleEngine;
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
        heartRuleEngine = new HeartRuleEngine(appContext);
        poiManager = new PoiManager(appContext);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        debugInfo();

        final ImmutableList<? extends WithStateElement> stateElements = ImmutableList.of(
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

        //TODO: infer preferred POI types / not acceptable POI types
        //TODO: filter or create new list of recommended POIs with new estimated rating and save
    }

    private void debugInfo() {
        log.debug(contextStorage.getContextProperties().toString());
    }
}
