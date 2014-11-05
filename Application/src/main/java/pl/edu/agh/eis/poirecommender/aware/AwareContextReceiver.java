package pl.edu.agh.eis.poirecommender.aware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.location.DetectedActivity;
import pl.edu.agh.eis.poirecommender.aware.model.Activity;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.aware.model.Weather;
import pl.edu.agh.eis.poirecommender.debug.AwareDebugContext;

import java.util.List;

import static com.aware.providers.Locations_Provider.Locations_Data;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareContextReceiver extends BroadcastReceiver {
    private static final String ACTIVITY_RECOGNITION_ACTION = "ACTION_AWARE_GOOGLE_ACTIVITY_RECOGNITION";
    private static final String WEATHER_ACTION = "ACTION_AWARE_WEATHER";
    private static final String LOCATIONS_ACTION = "ACTION_AWARE_LOCATIONS";

    @Override
    public void onReceive(Context context, Intent intent) {
        AwareContext awareContext = new AwareContext(context);
        switch (intent.getAction()) {
            case ACTIVITY_RECOGNITION_ACTION:
                final Activity activity = new Activity(
                        intent.getStringExtra("activity"),
                        intent.getIntExtra("confidence", DetectedActivity.UNKNOWN)
                );
                AwareDebugContext.getInstance().addAwareNotification(activity);
                awareContext.updateActivity(activity);
                break;
            case WEATHER_ACTION:
                final Weather weather = new Weather(
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
                        intent.getDoubleExtra(Weather.CLOUDS_VALUE, 0),
                        intent.getStringExtra(Weather.WEATHER_PROVIDER),
                        (List<String>) intent.getSerializableExtra(Weather.WEATHER_NAME)
                );
                AwareDebugContext.getInstance().addAwareNotification(weather);
                awareContext.updateWeather(weather);
                break;
            case LOCATIONS_ACTION:
                final Location location = new Location(
                        intent.getLongExtra(Locations_Data.TIMESTAMP, 0),
                        intent.getDoubleExtra(Locations_Data.LATITUDE, 0),
                        intent.getDoubleExtra(Locations_Data.LONGITUDE, 0),
                        intent.getFloatExtra(Locations_Data.BEARING, 0),
                        intent.getFloatExtra(Locations_Data.SPEED, 0),
                        intent.getDoubleExtra(Locations_Data.ALTITUDE, 0),
                        intent.getFloatExtra(Locations_Data.ACCURACY, 0)
                );
                AwareDebugContext.getInstance().addAwareNotification(location);
                awareContext.updateLocation(location);
                break;
        }
    }
}
