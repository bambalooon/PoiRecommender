package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import android.content.SharedPreferences;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Krzysztof Balon on 2014-10-31.
 */
public class AwarePreferences {
    private static final String AWARE_PREFERENCES = "AWARE_PREFERENCES";
    private static final String ACTIVITY_PREFERENCE = "ACTIVITY";
    private static final String WEATHER_PREFERENCE = "WEATHER";
    private static final String LATITUDE_PREFERENCE = "LATITUDE";
    private static final String LONGITUDE_PREFERENCE = "LONGITUDE";
    private static final long NO_LATITUDE = Double.doubleToRawLongBits(100);
    private static final long NO_LONGITUDE = Double.doubleToLongBits(400);
    private static final int ACTIVITY_MIN_CONFIDENCE = 70;
    private static final double LOCATION_MIN_DISTANCE = 0.005;
    private SharedPreferences awarePreferences;

    public AwarePreferences(Context context) {
        this.awarePreferences = context.getSharedPreferences(AWARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean areAllPreferencesSet() {
        return getActivity() != null && getWeather() != null && getLatitude() != null && getLongitude() != null;
    }

    public String getActivity() {
        return awarePreferences.getString(ACTIVITY_PREFERENCE, null);
    }

    public String getWeather() {
        return awarePreferences.getString(WEATHER_PREFERENCE, null);
    }

    public Double getLatitude() {
        long latitudeRaw = awarePreferences.getLong(LATITUDE_PREFERENCE, NO_LATITUDE);
        return latitudeRaw == NO_LATITUDE ? null : Double.longBitsToDouble(latitudeRaw);
    }

    public Double getLongitude() {
        long longitudeRaw = awarePreferences.getLong(LONGITUDE_PREFERENCE, NO_LONGITUDE);
        return longitudeRaw == NO_LONGITUDE ? null : Double.longBitsToDouble(longitudeRaw);
    }

    public boolean setActivity(String activity) {
        return awarePreferences.edit()
                .putString(ACTIVITY_PREFERENCE, activity)
                .commit();
    }

    public boolean setWeather(String weather) {
        return awarePreferences.edit()
                .putString(WEATHER_PREFERENCE, weather)
                .commit();
    }

    public boolean setLatitude(double latitude) {
        return awarePreferences.edit()
                .putLong(LATITUDE_PREFERENCE, Double.doubleToRawLongBits(latitude))
                .commit();
    }

    public boolean setLongitude(double longitude) {
        return awarePreferences.edit()
                .putLong(LONGITUDE_PREFERENCE, Double.doubleToRawLongBits(longitude))
                .commit();
    }

    public boolean isActivityDifferent(String activity, int confidence) {
        return confidence >= ACTIVITY_MIN_CONFIDENCE && !activity.equals(getActivity());
    }

    public boolean isWeatherDifferent(String weather) {
        return !weather.equals(getWeather());
    }

    public boolean isLocationDifferent(double latitude, double longitude, float accuracy) {
        //TODO: add accuracy to equation
        return getLatitude() == null || getLongitude() == null ||
                getApproxDistance(latitude, getLatitude(), longitude, getLongitude()) > LOCATION_MIN_DISTANCE;
    }

    private double getApproxDistance(double lat1, double lat2, double lon1, double lon2) {
        return sqrt(pow(lat1 - lat2, 2.0) + pow(lon1 - lon2, 2.0));
    }
}
