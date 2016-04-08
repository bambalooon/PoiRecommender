package pl.edu.agh.eis.poirecommender.openstreetmap;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.IdConstraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.NoArea;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Node;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Query;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.QueryPart;

public class ToNodeWithNoArea implements Function<QueryPart, Query> {
    private static final NoArea NO_AREA = new NoArea();

    @Override
    public Query apply(QueryPart queryPart) {
        Preconditions.checkArgument(queryPart instanceof IdConstraint,
                "NoArea can't be used without IdConstraint type QueryPart");
        return new Node(NO_AREA, queryPart);
    }
}
