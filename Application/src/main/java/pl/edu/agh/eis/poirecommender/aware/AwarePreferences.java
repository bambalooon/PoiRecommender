package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import pl.edu.agh.eis.poirecommender.aware.model.Activity;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.aware.model.Weather;

import java.util.Date;

/**
 * Created by Krzysztof Balon on 2014-10-31.
 */
public class AwarePreferences {
    private static final Gson GSON_SERIALIZER = new Gson();
    private static final String AWARE_PREFERENCES = "AWARE_PREFERENCES";
    private static final String ACTIVITY_PREFERENCE = "ACTIVITY";
    private static final String WEATHER_PREFERENCE = "WEATHER";
    private static final String LOCATION_PREFERENCE = "LOCATION";
    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = 60 * MINUTE;
    private static final int ACTIVITY_EXPIRATION = 5 * MINUTE;
    private static final int WEATHER_EXPIRATION = 6 * HOUR;
    private static final int LOCATION_EXPIRATION = 5 * MINUTE;

    private SharedPreferences awarePreferences;

    public AwarePreferences(Context context) {
        this.awarePreferences = context.getSharedPreferences(AWARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean areAllPreferencesSet() {
        return getActivity() != null && getWeather() != null && getLocation() != null;
    }

    public Activity getActivity() {
        final String activityJson = awarePreferences.getString(ACTIVITY_PREFERENCE, null);
        final Activity activity = GSON_SERIALIZER.fromJson(activityJson, Activity.class);
        if(activity != null && (getNow() - activity.getTimestamp()) > ACTIVITY_EXPIRATION) {
            setActivity(null);
            return null;
        }
        return activity;
    }

    public Weather getWeather() {
        final String weatherJson = awarePreferences.getString(WEATHER_PREFERENCE, null);
        final Weather weather = GSON_SERIALIZER.fromJson(weatherJson, Weather.class);
        if(weather != null && (getNow() - weather.getForecastTimestamp()) > WEATHER_EXPIRATION) {
            setWeather(null);
            return null;
        }
        return weather;
    }

    public Location getLocation() {
        final String locationJson = awarePreferences.getString(LOCATION_PREFERENCE, null);
        final Location location = GSON_SERIALIZER.fromJson(locationJson, Location.class);
        if(location != null && (getNow() - location.getTimestamp()) > LOCATION_EXPIRATION) {
            setLocation(null);
            return null;
        }
        return location;
    }

    public boolean setActivity(Activity activity) {
        return awarePreferences.edit()
                .putString(ACTIVITY_PREFERENCE, GSON_SERIALIZER.toJson(activity))
                .commit();
    }

    public boolean setWeather(Weather weather) {
        return awarePreferences.edit()
                .putString(WEATHER_PREFERENCE, GSON_SERIALIZER.toJson(weather))
                .commit();
    }

    public boolean setLocation(Location location) {
        return awarePreferences.edit()
                .putString(LOCATION_PREFERENCE, GSON_SERIALIZER.toJson(location))
                .commit();
    }

    private long getNow() {
        return new Date().getTime();
    }
}
