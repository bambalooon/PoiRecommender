package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import heart.xtt.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
public class CompositeConstraintBuilder {
    private final List<Constraint> constraints = new LinkedList<>();
    private List<Constraint> andConstraints = new LinkedList<>();

    public CompositeConstraintBuilder(Constraint constraint) {
        andConstraints.add(constraint);
    }

    public CompositeConstraintBuilder and(Constraint constraint) {
        andConstraints.add(constraint);
        return this;
    }

    public CompositeConstraintBuilder or(Constraint constraint) {
        constraints.add(buildCompoundConstraint());
        andConstraints = new LinkedList<>();
        andConstraints.add(constraint);
        return this;
    }

    public CompositeConstraint build() {
        constraints.add(buildCompoundConstraint());
        return new CompositeConstraint(constraints);
    }

    private Constraint buildCompoundConstraint() {
        return andConstraints.size() > 1
                ? new CompoundConstraint(andConstraints)
                : andConstraints.get(0);
    }
}
