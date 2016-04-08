package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Node implements Query {
    private static final String NODE_REQUEST = "node";
    private final Area area;
    private final QueryPart queryPart;

    @Override
    public String createQuery() {
        return String.format("%s%s%s",
                NODE_REQUEST,
                area.createQueryPart(),
                queryPart.createQueryPart());
    }
}
