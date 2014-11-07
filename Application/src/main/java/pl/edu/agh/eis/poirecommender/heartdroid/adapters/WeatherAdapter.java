package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import pl.edu.agh.eis.poirecommender.aware.model.Weather;

import java.util.Date;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class WeatherAdapter extends AbstractStateAdapter<Weather> {
    private static final String WEATHER_ATTRIBUTE = "weather";
    private static final long HOUR = 3600000L;
    private static final float FORECAST_OLDEST = 10;

    public WeatherAdapter(Weather weather) {
        super(weather);
    }

    @Override
    protected String getAttributeName() {
        return WEATHER_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        return getAdaptee().getWeatherDescription().get(0);
    }

    @Override
    protected float calculateCertainty() {
        float forecastAge = (new Date().getTime() - getAdaptee().getForecastTimestamp()) / HOUR;
        return forecastAge >= FORECAST_OLDEST
                ? 0
                : (FORECAST_OLDEST - forecastAge)
                        /FORECAST_OLDEST;
    }
}
