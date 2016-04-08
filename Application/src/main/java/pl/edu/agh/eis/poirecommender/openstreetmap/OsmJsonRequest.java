package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.location.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.CompositeConstraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.IdConstraint;

import java.util.List;

public class OsmJsonRequest implements OsmRequest {
    private static final String REQUEST_START = "[out:json];";
    private final OsmXmlRequest xmlRequest;

    public OsmJsonRequest(CompositeConstraint compositeConstraint, Location location) {
        this(new OsmXmlRequest(compositeConstraint, location));
    }

    public OsmJsonRequest(Constraint constraint, Location location) {
        this(new OsmXmlRequest(constraint, location));
    }

    public OsmJsonRequest(List<IdConstraint> idConstraints) {
        this(new OsmXmlRequest(idConstraints));
    }

    protected OsmJsonRequest(OsmXmlRequest xmlRequest) {
        this.xmlRequest = xmlRequest;
    }

    @Override
    public String toString() {
        return String.format("%s%s",
                REQUEST_START,
                xmlRequest);
    }
}
