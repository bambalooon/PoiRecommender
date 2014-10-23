package pl.bb.poirecommender.application.aware;

import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BamBalooon on 2014-10-20.
 */
public class Activity {
    private final long timestamp;
    private final int activity;
    private final int confidence;

    public Activity(int activity, int confidence) {
        this.activity = activity;
        this.confidence = confidence;
        timestamp = Calendar.getInstance(Locale.getDefault()).getTimeInMillis();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timestamp))+" {activity: "+getActivityName(activity)+", confidence: "+confidence+"}";
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
