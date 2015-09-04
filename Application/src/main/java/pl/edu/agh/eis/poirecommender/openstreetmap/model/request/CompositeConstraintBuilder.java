package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import java.util.LinkedList;

public class CompositeConstraintBuilder {
    private final CompositeConstraint compositeConstraint;
    private LinkedList<Constraint> constraints;

    public CompositeConstraintBuilder(Constraint constraint) {
        compositeConstraint = new CompositeConstraint();
        constraints = newList(constraint);
    }

    public CompositeConstraintBuilder and(Constraint constraint) {
        constraints.add(constraint);
        return this;
    }

    public CompositeConstraintBuilder or(Constraint constraint) {
        compositeConstraint.addConstraint(buildCompoundConstraint());
        constraints = newList(constraint);
        return this;
    }

    public CompositeConstraint build() {
        compositeConstraint.addConstraint(buildCompoundConstraint());
        return compositeConstraint;
    }

    private Constraint buildCompoundConstraint() {
        return constraints.size() > 1
                ? new CompoundConstraint(constraints)
                : constraints.getFirst();
    }

    private static LinkedList<Constraint> newList(Constraint constraint) {
        LinkedList<Constraint> constraints = new LinkedList<>();
        constraints.add(constraint);
        return constraints;
    }
}
