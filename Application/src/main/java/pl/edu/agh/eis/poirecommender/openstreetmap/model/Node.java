package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class Node implements RequestPart {
    private static final char NODE_REQUEST_PART_START = '(';
    private static final char NODE_REQUEST_PART_END = ')';
    private static final String NODE_REQUEST = "node";
    private static final Joiner CONSTRAINTS_JOINER = Joiner.on("");
    private final Area area;
    private final List<Constraint> constraints;

    public Node(Area area, List<Constraint> constraints) {
        this.area = area;
        this.constraints = constraints;
    }

    @Override
    public String createRequestPart() {
        return String.format("%c%s%s%s%c",
                NODE_REQUEST_PART_START,
                NODE_REQUEST,
                area.createRequestPart(),
                FluentIterable.from(constraints).transform(new Function<Constraint, String>() {
                    @Override
                    public String apply(Constraint constraint) {
                        return constraint.createRequestPart();
                    }
                }).join(CONSTRAINTS_JOINER),
                NODE_REQUEST_PART_END);
    }
}
