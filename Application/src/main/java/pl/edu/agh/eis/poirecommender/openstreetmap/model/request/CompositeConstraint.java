package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class CompositeConstraint {
    private final List<Constraint> constraints;

    public CompositeConstraint() {
        constraints = new LinkedList<>();
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}
