package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.location.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.CompositeConstraint;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class OsmJsonRequest implements OsmRequest {
    private static final String REQUEST_START = "[out:json];";
    private final OsmXmlRequest xmlRequest;

    public OsmJsonRequest(CompositeConstraint compositeConstraint, Location location) {
        xmlRequest = new OsmXmlRequest(compositeConstraint, location);
    }

    @Override
    public String toString() {
        return String.format("%s%s",
                REQUEST_START,
                xmlRequest);
    }
}
