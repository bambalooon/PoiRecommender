package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DayPartAdapter extends AbstractSymbolicStateAdapter<Date> {
    public static final String DAY_PART_ATTRIBUTE = "day_part";
    private static final Map<Range<Integer>, String> DAY_PART_CLASSIFICATION = ImmutableMap.
            <Range<Integer>, String>builder()
            .put(Range.atMost(5), "n")
            .put(Range.closed(6, 8), "em")
            .put(Range.closed(9, 11), "lm")
            .put(Range.closed(12, 14), "ea")
            .put(Range.closed(15, 17), "la")
            .put(Range.closed(18, 20), "ee")
            .put(Range.closed(21, 22), "le")
            .put(Range.atLeast(23), "n")
            .build();

    public DayPartAdapter(Date adaptee) {
        super(adaptee);
    }

    @Override
    protected String getAttributeName() {
        return DAY_PART_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getAdaptee());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        for (Range<Integer> datePartRange : DAY_PART_CLASSIFICATION.keySet()) {
            if (datePartRange.contains(hour)) {
                return DAY_PART_CLASSIFICATION.get(datePartRange);
            }
        }
        return null;
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
