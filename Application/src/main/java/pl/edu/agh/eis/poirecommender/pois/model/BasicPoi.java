package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.Element;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class BasicPoi implements Poi {
    private static final String OSM_LOCATION_PROVIDER = "OpenStreetMap";
    private final String name;
    private final Location location;

    public static BasicPoi fromOsmElement(Element osmElement) {
        Location location = new Location(OSM_LOCATION_PROVIDER);
        location.setLatitude(osmElement.getLat());
        location.setLongitude(osmElement.getLon());
        return new BasicPoi(
                osmElement.getTags() == null
                        ? null
                        : osmElement.getTags().getName(),
                location);
    }

    public BasicPoi(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
