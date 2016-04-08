package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.location.Location;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.AroundArea;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Node;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Query;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.QueryPart;

public class ToNodeWithAroundArea implements Function<QueryPart, Query> {
    private static final float DEFAULT_RADIUS = 5000; //[m]
    private final Location location;

    public ToNodeWithAroundArea(Location location) {
        this.location = location;
    }

    @Override
    public Query apply(QueryPart queryPart) {
        Preconditions.checkArgument(queryPart instanceof Constraint,
                "AroundArea can't be used without Constraint type QueryPart");
        return new Node(new AroundArea(location, DEFAULT_RADIUS), queryPart);
    }
}
