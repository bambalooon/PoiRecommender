package pl.edu.agh.eis.poirecommender.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeProvider {
    public long getTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }
    public Date getDate() {
        return Calendar.getInstance().getTime();
    }
}
