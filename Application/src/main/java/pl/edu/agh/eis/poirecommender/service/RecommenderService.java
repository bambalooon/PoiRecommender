package pl.edu.agh.eis.poirecommender.service;

import android.app.IntentService;
import android.content.Intent;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.storage.ContextStorage;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.plugin.openweather.Provider;
import com.aware.poirecommender.provider.PoiRecommenderContract;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.heartdroid.HeartRuleEngine;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.DayPartAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.RainAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.TemperatureAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WeekDayAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WindAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.CompositeConstraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Evaluable;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.utils.DateTimeProvider;

import java.util.Date;
import java.util.List;

/**
 * Name: RecommenderService
 * Description: RecommenderService
 * Date: 2014-10-30
 * Created by BamBalooon
 */
@Slf4j
public class RecommenderService extends IntentService {
    private static final String RECOMMENDER_SERVICE_NAME = "PoiRecommender::Service";
    private final DateTimeProvider dateTimeProvider = new DateTimeProvider();
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

        GenericContextProperty weatherContextProperty = contextStorage
                .getContextProperty(PoiRecommenderContract.Contexts.PLUGIN_OPENWEATHER_TIMESTAMP);
        Date date = dateTimeProvider.getDate();
        final List<? extends WithStateElement> stateElements = ImmutableList.of(
                new DayPartAdapter(date),
                new WeekDayAdapter(date),
                new TemperatureAdapter(weatherContextProperty, Provider.OpenWeather_Data.TEMPERATURE),
                new WindAdapter(weatherContextProperty, Provider.OpenWeather_Data.WIND_SPEED),
                new RainAdapter(weatherContextProperty, Provider.OpenWeather_Data.RAIN));

        PoiType recommendedPoiType = heartRuleEngine.inferencePreferredPoiType(stateElements).getPoiType();
        CompositeConstraint recommendedPoisConstraint = PoiTypeToConstraintMap.getConstraint(recommendedPoiType);

        PoiStorage recommendedPois = poiManager.getRecommendedPois();

        List<? extends Poi> filteredRecommendedPois = FluentIterable.from(recommendedPois.getPoiList())
                .filter(new EvaluablePredicate(recommendedPoisConstraint))
                .toList();

        poiManager.setFilteredRecommendedPois(new PoiStorage(filteredRecommendedPois));
    }

    private void debugInfo() {
        log.debug(contextStorage.getContextProperties().toString());
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private class EvaluablePredicate implements Predicate<Poi> {
        private final Evaluable evaluable;

        @Override
        public boolean apply(Poi poi) {
            return evaluable.eval(poi);
        }
    }
}
