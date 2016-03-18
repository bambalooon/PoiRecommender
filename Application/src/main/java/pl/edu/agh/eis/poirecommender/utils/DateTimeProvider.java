package pl.edu.agh.eis.poirecommender.utils;

import java.util.Calendar;

public class DateTimeProvider {
    public long getTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
