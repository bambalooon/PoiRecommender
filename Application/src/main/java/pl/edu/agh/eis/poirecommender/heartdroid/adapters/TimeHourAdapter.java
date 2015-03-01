package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Krzysztof Balon on 2015-01-17.
 */
public class TimeHourAdapter extends AbstractNumericStateAdapter<Date> {
    public static final String HOUR_ATTRIBUTE = "hour";
    private static final float TIME_CERTAINTY = 1.0f;

    public TimeHourAdapter(Date adaptee) {
        super(adaptee);
    }

    @Override
    protected String getAttributeName() {
        return HOUR_ATTRIBUTE;
    }

    @Override
    protected Double adaptValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getAdaptee());
        return (double) calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    protected float calculateCertainty() {
        return TIME_CERTAINTY;
    }
}
