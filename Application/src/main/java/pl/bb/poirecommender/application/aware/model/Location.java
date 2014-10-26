package pl.bb.poirecommender.application.aware.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Krzysztof Balon on 2014-10-25.
 */
public class Location {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final long timestamp;
    private final double latitude;
    private final double longitude;
    private final float bearing;
    private final float speed;
    private final double altitude;
    private final float accuracy;

    public Location(long timestamp, double latitude, double longitude, float bearing, float speed, double altitude, float accuracy) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.speed = speed;
        this.altitude = altitude;
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s {lat: %.6f°, lon: %.6f°, bearing: %.1f, speed: %.1f, alt: %.1f, acc: %.0f}",
                DATE_FORMAT.format(timestamp),
                latitude,
                longitude,
                bearing,
                speed,
                altitude,
                accuracy);
    }
}
