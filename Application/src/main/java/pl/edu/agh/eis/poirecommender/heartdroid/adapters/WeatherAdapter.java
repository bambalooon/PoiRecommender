package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.google.common.collect.ImmutableMap;
import pl.edu.agh.eis.poirecommender.aware.model.Weather;

import java.util.Date;
import java.util.Map;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class WeatherAdapter extends AbstractStateAdapter<Weather> {
    private static final String WEATHER_ATTRIBUTE = "weather";
    private static final long HOUR = 3600000L;
    private static final float FORECAST_OLDEST = 6;
    private static final int NO_CERTAINTY = 0;
    private static final Map<String, String> WEATHER_NAME_MAPPING = ImmutableMap.<String, String>builder()
            .put("clear sky", "sunny")
            .put("few clouds", "sunny")
            .put("scattered clouds", "cloudy")
            .put("broken clouds", "cloudy")
            .put("shower rain", "rainy")
            .put("light rain", "rainy")
            .put("rain", "rainy")
            .put("thunderstorm", "rainy")
            .put("snow", "rainy")
            .put("mist", "cloudy")
            .put("fog", "cloudy")
            .build();

    public WeatherAdapter(Weather weather) {
        super(weather);
    }

    @Override
    protected String getAttributeName() {
        return WEATHER_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        final Weather weather = getAdaptee();
        return weather != null && weather.getWeatherDescription() != null && !weather.getWeatherDescription().isEmpty()
                ? getMappedWeatherName(weather.getWeatherDescription().get(0))
                : null;
    }

    @Override
    protected float calculateCertainty() {
        final Weather weather = getAdaptee();
        if(weather == null) {
            return NO_CERTAINTY;
        }
        float forecastAge = (getNow() - weather.getForecastTimestamp()) / HOUR;
        return forecastAge >= FORECAST_OLDEST
                ? NO_CERTAINTY
                : (FORECAST_OLDEST - forecastAge)
                        /FORECAST_OLDEST;
    }

    private long getNow() {
        return new Date().getTime();
    }

    private String getMappedWeatherName(String weatherName) {
        return WEATHER_NAME_MAPPING.get(weatherName.toLowerCase());
    }
}
