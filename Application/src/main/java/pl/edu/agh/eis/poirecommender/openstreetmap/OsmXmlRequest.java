package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.location.Location;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.CompositeConstraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Query;

import java.util.Collections;
import java.util.List;

public class OsmXmlRequest implements OsmRequest {
    private static final Joiner QUERY_SEPARATOR = Joiner.on(';');
    private static final String REQUEST_START = "(";
    private static final String REQUEST_END = ");out;";
    private final List<Query> queries;

    public OsmXmlRequest(CompositeConstraint compositeConstraint, Location location) {
        this(compositeConstraint.getConstraints(), location);
    }

    public OsmXmlRequest(Constraint constraint, Location location) {
        this(Collections.singletonList(constraint), location);
    }

    //TODO: ToNodeWithAroundArea should be extracted outside to avoid classes coupling
    protected OsmXmlRequest(List<Constraint> constraints, Location location) {
        ToNodeWithAroundArea toNodeWithAroundArea = new ToNodeWithAroundArea(location);
        this.queries = FluentIterable.from(constraints)
                .transform(toNodeWithAroundArea)
                .toList();
    }

    @Override
    public String toString() {
        return String.format("%s%s%s",
                REQUEST_START,
                FluentIterable.from(queries)
                        .transform(new Function<Query, String>() {
                            @Override
                            public String apply(Query query) {
                                return query.createQuery();
                            }
                        }).join(QUERY_SEPARATOR),
                REQUEST_END);
    }
}
