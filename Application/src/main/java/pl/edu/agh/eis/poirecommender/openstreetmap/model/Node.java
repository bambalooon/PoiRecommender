package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class Node implements Query {
    private static final char NODE_REQUEST_PART_END = ';';
    private static final String NODE_REQUEST = "node";
    private static final Joiner CONSTRAINTS_JOINER = Joiner.on("");
    private final Area area;
    private final List<Constraint> constraints;

    public Node(Area area, List<Constraint> constraints) {
        this.area = area;
        this.constraints = constraints;
    }

    @Override
    public String createQuery() {
        return String.format("%s%s%s%c",
                NODE_REQUEST,
                area.createQueryPart(),
                FluentIterable.from(constraints).transform(new Function<Constraint, String>() {
                    @Override
                    public String apply(Constraint constraint) {
                        return constraint.createQueryPart();
                    }
                }).join(CONSTRAINTS_JOINER),
                NODE_REQUEST_PART_END);
    }
}