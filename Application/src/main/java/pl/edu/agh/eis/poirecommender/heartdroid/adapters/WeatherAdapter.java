package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import pl.edu.agh.eis.poirecommender.aware.model.Weather;

import java.util.Date;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class WeatherAdapter extends AbstractStateAdapter<Weather> {
    private static final String WEATHER_ATTRIBUTE = "weather";
    private static final long HOUR = 3600000L;
    private static final float FORECAST_OLDEST = 6;
    private static final int NO_CERTAINTY = 0;

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
                ? weather.getWeatherDescription().get(0)
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
}
