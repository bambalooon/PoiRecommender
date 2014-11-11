package pl.edu.agh.eis.poirecommender.aware.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Krzysztof Balon on 2014-10-25.
 */
public class Location implements SignificantlyDifferent<Location> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final double SIGNIFICANTLY_GREAT_DISTANCE = 0.005;
    public static final double DEGREES_TO_KM_FACTOR = 111.32;

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

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getAccuracy() {
        return accuracy;
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

    @Override
    public boolean isSignificantlyDifferent(Location other) {
        return other == null
                ? true
                : timestamp != other.getTimestamp()
                && calculateAproxDistance(latitude, other.getLatitude(), longitude, getLongitude()) >= SIGNIFICANTLY_GREAT_DISTANCE;
    }

    public static double calculateAproxDistance(double lat1, double lat2, double lon1, double lon2) {
        return sqrt(pow(lat1 - lat2, 2) + pow(lon1 - lon2, 2));
    }
    
    public static double degreesToKiloMeters(double degree) {
        return degree * DEGREES_TO_KM_FACTOR;
    }
}
