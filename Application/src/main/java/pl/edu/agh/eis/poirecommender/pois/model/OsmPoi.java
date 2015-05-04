package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;

/**
 * Name: OsmPoi
 * Description: OsmPoi
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class OsmPoi implements Poi {
    public static final String OSM_LOCATION_PROVIDER = "OpenStreetMap";
    private final String name;
    private final Location location;
    private final Element osmElement;

    public static OsmPoi fromOsmElement(Element osmElement) {
        Location location = new Location(OSM_LOCATION_PROVIDER);
        location.setLatitude(osmElement.getLat());
        location.setLongitude(osmElement.getLon());
        return new OsmPoi(
                osmElement.getTags() == null
                        ? null
                        : osmElement.getTags().get(Element.ELEMENT_NAME_TAG),
                location,
                osmElement);
    }

    public OsmPoi(String name, Location location, Element osmElement) {
        this.name = name;
        this.location = location;
        this.osmElement = osmElement;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Element getElement() {
        return osmElement;
    }
}
