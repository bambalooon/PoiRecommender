package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class Node implements Query {
    private static final String NODE_REQUEST = "node";
    private final Area area;
    private final Constraint constraint;

    public Node(Area area, Constraint constraint) {
        this.area = area;
        this.constraint = constraint;
    }

    @Override
    public String createQuery() {
        return String.format("%s%s%s",
                NODE_REQUEST,
                area.createQueryPart(),
                constraint.createQueryPart());
    }
}