package pl.edu.agh.eis.poirecommender.aware.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Krzysztof Balon on 2014-10-20.
 */
public class Activity implements SignificantlyDifferent<Activity> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final int CONFIDENCE_SIGNIFICANTLY_DIFFERENT = 5;
    private final long timestamp;
    private final String activityName;
    private final int confidence;

    public Activity(String activityName, int confidence) {
        this.activityName = activityName;
        this.confidence = confidence;
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s {activity: %s, confidence: %d}",
                DATE_FORMAT.format(new Date(timestamp)),
                activityName,
                confidence);
    }

    @Override
    public boolean isSignificantlyDifferent(Activity other) {
        return other == null
                ? true
                : !activityName.equals(other.getActivityName())
                || Math.abs(confidence - other.getConfidence()) >= CONFIDENCE_SIGNIFICANTLY_DIFFERENT;
    }
}
