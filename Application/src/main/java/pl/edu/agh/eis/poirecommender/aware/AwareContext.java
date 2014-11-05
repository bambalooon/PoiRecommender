package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import pl.edu.agh.eis.poirecommender.aware.model.Activity;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.aware.model.Weather;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

/**
 * Created by Krzysztof Balon on 2014-10-31.
 */
public class AwareContext {
    private final Context context;
    private final AwarePreferences awarePreferences;

    public AwareContext(Context context) {
        this.context = context;
        this.awarePreferences = new AwarePreferences(context);
    }

    public void updateActivity(Activity activity) {
        final String activityName = activity.getActivity();
        final int confidence = activity.getConfidence();
        if(awarePreferences.isActivityDifferent(activityName, confidence) &&
                awarePreferences.setActivity(activityName)) {
            RecommenderService.notifyRecommender(context);
        }
    }

    public void updateWeather(Weather weather) {
        final String weatherName = weather.getWeatherDescription().get(0);
        if(awarePreferences.isWeatherDifferent(weatherName) && awarePreferences.setWeather(weatherName)) {
            RecommenderService.notifyRecommender(context);
        }
    }

    public void updateLocation(Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        final float accuracy = location.getAccuracy();
        if(awarePreferences.isLocationDifferent(latitude, longitude, accuracy)
                && awarePreferences.setLatitude(latitude)
                && awarePreferences.setLongitude(longitude)) {
            RecommenderService.notifyRecommender(context);
        }
    }

}
