package pl.bb.poirecommender.application.aware.model;

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
    private final int activity;
    private final int confidence;

    public Activity(int activity, int confidence) {
        this.activity = activity;
        this.confidence = confidence;
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
    }

    @Override
    public String toString() {
        return String.format("%s {activity: %s, confidence: %d}",
                DATE_FORMAT.format(new Date(timestamp)),
                getActivityName(activity),
                confidence);
    }

    private static String getActivityName(int type) {
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.WALKING:
                return "walking";
            case DetectedActivity.RUNNING:
                return "running";
            default:
                return "unknown";
        }
    }
}
