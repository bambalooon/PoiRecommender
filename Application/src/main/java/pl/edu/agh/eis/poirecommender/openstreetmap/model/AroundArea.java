package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import pl.edu.agh.eis.poirecommender.aware.model.Location;

import java.util.Locale;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class AroundArea implements Area {
    private static final String AROUND_RESTRICTOR = "around:";
    private final Location location;
    private final float radius;

    public AroundArea(Location location, float radius) {
        this.location = location;
        this.radius = radius;
    }

    @Override
    public String createRequestPart() {
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
