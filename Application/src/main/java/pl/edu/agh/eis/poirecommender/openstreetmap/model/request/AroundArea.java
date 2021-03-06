package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.location.Location;

import java.util.Locale;

public class AroundArea implements Area {
    private static final String AROUND_RESTRICTOR = "around:";
    private final Location location;
    private final float radius;

    public AroundArea(Location location, float radius) {
        this.location = location;
        this.radius = radius;
    }

    @Override
    public String createQueryPart() {
        return String.format(Locale.US, "%c%s%.2f%c%.6f%c%.6f%c",
                AREA_REQUEST_PART_START,
                AROUND_RESTRICTOR,
                radius,
                PROPERTY_SEPARATOR,
                location.getLatitude(),
                PROPERTY_SEPARATOR,
                location.getLongitude(),
                AREA_REQUEST_PART_END);
    }
}
