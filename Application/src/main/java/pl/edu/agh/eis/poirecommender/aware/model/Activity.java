package pl.edu.agh.eis.poirecommender.aware.model;

import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Krzysztof Balon on 2014-10-20.
 */
public class Activity {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
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
}
