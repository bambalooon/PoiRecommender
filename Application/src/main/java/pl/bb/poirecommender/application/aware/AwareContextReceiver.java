package pl.bb.poirecommender.application.aware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.DetectedActivity;
import pl.bb.poirecommender.application.aware.model.Activity;
import pl.bb.poirecommender.application.aware.model.Weather;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareContextReceiver extends BroadcastReceiver {
    private static final String TAG = AwareContextReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received: " + intent.toString());
        switch (intent.getAction()) {
            case "ACTION_AWARE_GOOGLE_ACTIVITY_RECOGNITION":
                AwareContext.getInstance().addAwareNotification(
                        new Activity(
                                intent.getIntExtra("activity", DetectedActivity.UNKNOWN),
                                intent.getIntExtra("confidence", DetectedActivity.UNKNOWN)
                        ));
                break;
            case "ACTION_AWARE_WEATHER":
                AwareContext.getInstance().addAwareNotification(
                        new Weather(
                                intent.getLongExtra(Weather.FORECAST_TIMESTAMP, 0L),
                                intent.getLongExtra(Weather.REQUEST_TIMESTAMP, 0L),
                                intent.getStringExtra(Weather.DATE_TIME),
                                intent.getStringExtra(Weather.LOCATION_NAME),
                                intent.getDoubleExtra(Weather.LATITUDE, 0),
                                intent.getDoubleExtra(Weather.LONGITUDE, 0),
                                intent.getLongExtra(Weather.SUNRISE, 0L),
                                intent.getLongExtra(Weather.SUNSET, 0L),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_CURRENT, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_DAY, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_NIGHT, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_MAX, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_MIN, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_MORNING, 0),
                                intent.getDoubleExtra(Weather.TEMPERATURE_VALUE_EVENING, 0),
                                intent.getDoubleExtra(Weather.PRESSURE, 0),
                                intent.getDoubleExtra(Weather.HUMIDITY, 0),
                                intent.getDoubleExtra(Weather.WIND_SPEED, 0),
                                intent.getDoubleExtra(Weather.WIND_GUST, 0),
                                intent.getDoubleExtra(Weather.WIND_ANGLE, 0),
                                intent.getDoubleExtra(Weather.RAIN, 0),
                                intent.getStringExtra(Weather.WEATHER_PROVIDER)
                        ));
                break;
            default:
                Log.d(TAG, "Received: " + intent.toString());
        }
    }


}
