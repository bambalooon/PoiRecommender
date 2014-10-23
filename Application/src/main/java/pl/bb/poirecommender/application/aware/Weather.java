package pl.bb.poirecommender.application.aware;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BamBalooon on 2014-10-20.
 */
public class Weather {
    public static final String REQUEST_TIMESTAMP = "timestamp";
    public static final String FORECAST_TIMESTAMP = "double_forecast_timestamp";
    public static final String DEVICE_ID = "device_id";
    public static final String LATITUDE = "double_latitude";
    public static final String LONGITUDE = "double_longitude";
    public static final String TEMPERATURE_VALUE_CURRENT = "temperature_value_current";
    public static final String TEMPERATURE_VALUE_MIN = "temperature_value_min";
    public static final String TEMPERATURE_VALUE_MAX = "temperature_value_max";
    public static final String TEMPERATURE_VALUE_NIGHT = "temperature_value_night";
    public static final String TEMPERATURE_VALUE_DAY = "temperature_value_day";
    public static final String TEMPERATURE_VALUE_EVENING = "temperature_value_evening";
    public static final String TEMPERATURE_VALUE_MORNING = "temperature_value_morning";
    public static final String HUMIDITY = "humidity";
    public static final String PRESSURE = "pressure";
    public static final String WIND_SPEED = "wind_speed";
    public static final String WIND_GUST = "wind_speed_gust";
    public static final String WIND_ANGLE = "wind_angle";
    public static final String CLOUDS_VALUE = "clouds_value";
    public static final String RAIN = "rain";
    public static final String WEATHER_NAME = "weather_name";
    public static final String WEATHER_PROVIDER = "weather_provider";
    public static final String SUNRISE = "sunrise";
    public static final String SUNSET = "sunset";
    public static final String LOCATION_NAME = "location_name";
    public static final String DATE_TIME = "date_time";

    private final long timestamp;
    private final long forecastTimestamp;
    private final long requestTimestamp;
    private final String dateTime;
    private final String locationName;
    private final double lat;
    private final double lon;
    private final long sunrise;
    private final long sunset;
    private final double tempCurrent;
    private final double tempDay;
    private final double tempNight;
    private final double tempMax;
    private final double tempMin;
    private final double tempMorning;
    private final double tempEvening;
    private final double pressure;
    private final double humidity;
    private final double windSpeed;
    private final double windGust;
    private final double windAngle;
    private final double rain;
    private final String weatherProvider;

    public Weather(long forecastTimestamp, long requestTimestamp, String dateTime, String locationName, double lat, double lon, long sunrise, long sunset, double tempCurrent, double tempDay, double tempNight, double tempMax, double tempMin, double tempMorning, double tempEvening, double pressure, double humidity, double windSpeed, double windGust, double windAngle, double rain, String weatherProvider) {
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();

        this.forecastTimestamp = forecastTimestamp;
        this.requestTimestamp = requestTimestamp;
        this.dateTime = dateTime;
        this.locationName = locationName;
        this.lat = lat;
        this.lon = lon;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.tempCurrent = tempCurrent;
        this.tempDay = tempDay;
        this.tempNight = tempNight;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.tempMorning = tempMorning;
        this.tempEvening = tempEvening;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windGust = windGust;
        this.windAngle = windAngle;
        this.rain = rain;
        this.weatherProvider = weatherProvider;
    }

    public double getTempCurrentInCelsius() {
        return (tempCurrent-32)/1.8;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timestamp))
                + "{f: " + sdf.format(new Date(forecastTimestamp)) + ", "
                + locationName + ", "
                + "temp: " + String.format("%.0f", getTempCurrentInCelsius()) + "\u00B0C, "
                + "lat: " + lat + ", "
                + "lon: " + lon + ", "
                + "rain: " + rain + "}";
    }
}
