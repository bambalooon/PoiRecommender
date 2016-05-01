package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.google.common.collect.ImmutableMap;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class WeekDayAdapter extends AbstractSymbolicStateAdapter<Date> {
    public static final String WEEKDAY_ATTRIBUTE = "weekday";
    private static final Map<Integer, String> WEEKDAY_CLASSIFICATION = ImmutableMap.<Integer, String>builder()
            .put(Calendar.MONDAY, "mon")
            .put(Calendar.TUESDAY, "tue")
            .put(Calendar.WEDNESDAY, "wed")
            .put(Calendar.THURSDAY, "thu")
            .put(Calendar.FRIDAY, "fri")
            .put(Calendar.SATURDAY, "sat")
            .put(Calendar.SUNDAY, "sun")
            .build();

    public WeekDayAdapter(Date adaptee) {
        super(adaptee);
    }

    @Override
    protected String getAttributeName() {
        return WEEKDAY_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getAdaptee());
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);

        return WEEKDAY_CLASSIFICATION.get(weekday);
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
