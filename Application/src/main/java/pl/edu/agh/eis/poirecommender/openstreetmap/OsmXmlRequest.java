package pl.edu.agh.eis.poirecommender.openstreetmap;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.*;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class OsmXmlRequest implements OsmRequest {
    private static final Joiner QUERY_SEPARATOR = Joiner.on(';');
    private static final String REQUEST_START = "(";
    private static final String REQUEST_END = ");out;";
    private final List<Query> queries;

    public OsmXmlRequest(CompositeConstraint compositeConstraint, Location location) {
        final ToNodeWithAroundArea toNodeWithAroundArea = new ToNodeWithAroundArea(location);
        this.queries = FluentIterable.from(compositeConstraint.getConstraints())
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
