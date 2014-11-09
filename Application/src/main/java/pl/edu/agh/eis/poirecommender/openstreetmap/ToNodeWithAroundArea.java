package pl.edu.agh.eis.poirecommender.openstreetmap;

import com.google.common.base.Function;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.AroundArea;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Node;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Query;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class ToNodeWithAroundArea implements Function<Constraint, Query> {
    private static final float DEFAULT_RADIUS = 5000; //[m]
    private final Location location;

    public ToNodeWithAroundArea(Location location) {
        this.location = location;
    }

    @Override
    public Query apply(Constraint constraint) {
        return new Node(new AroundArea(location, DEFAULT_RADIUS), constraint);
    }
}
