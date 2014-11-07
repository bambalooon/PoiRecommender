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
        if(activity.isSignificantlyDifferent(awarePreferences.getActivity()) && awarePreferences.setActivity(activity)) {
            RecommenderService.notifyRecommender(context);
        }
    }

    public void updateWeather(Weather weather) {
        if(weather.isSignificantlyDifferent(awarePreferences.getWeather()) && awarePreferences.setWeather(weather)) {
            RecommenderService.notifyRecommender(context);
        }
    }

    public void updateLocation(Location location) {
        if(location.isSignificantlyDifferent(awarePreferences.getLocation()) && awarePreferences.setLocation(location)) {
            RecommenderService.notifyRecommender(context);
        }
    }
}
