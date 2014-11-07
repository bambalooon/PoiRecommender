package pl.edu.agh.eis.poirecommender.aware.model;

import com.google.android.gms.location.DetectedActivity;

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
    private final String activity;
    private final int confidence;

    public Activity(String activity, int confidence) {
        this.activity = activity;
        this.confidence = confidence;
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getActivity() {
        return activity;
    }

    public int getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s {activity: %s, confidence: %d}",
                DATE_FORMAT.format(new Date(timestamp)),
                activity,
                confidence);
    }

    @Override
    public boolean isSignificantlyDifferent(Activity other) {
        return other == null
                ? true
                : !activity.equals(other.getActivity())
                || Math.abs(confidence - other.getConfidence()) >= CONFIDENCE_SIGNIFICANTLY_DIFFERENT;
    }
}
