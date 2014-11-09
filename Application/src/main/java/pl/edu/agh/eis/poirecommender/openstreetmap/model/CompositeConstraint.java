package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class CompositeConstraint {
    private final List<Constraint> constraints;

    public CompositeConstraint(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}
