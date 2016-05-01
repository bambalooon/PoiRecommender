package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import pl.edu.agh.eis.poirecommender.pois.model.Poi;

import java.util.LinkedList;
import java.util.List;

public class CompositeConstraint implements Evaluable {
    private final List<Constraint> constraints = new LinkedList<>();

    private CompositeConstraint() {
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public boolean eval(Poi poi) {
        for (Constraint constraint : constraints) {
            if (constraint.eval(poi)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private final CompositeConstraint compositeConstraint = new CompositeConstraint();
        private LinkedList<Constraint> constraints;

        public Builder(Constraint constraint) {
            constraints = newList(constraint);
        }

        public Builder and(Constraint constraint) {
            constraints.add(constraint);
            return this;
        }

        public Builder or(Constraint constraint) {
            compositeConstraint.constraints.add(buildCompoundConstraint());
            constraints = newList(constraint);
            return this;
        }

        public CompositeConstraint build() {
            compositeConstraint.constraints.add(buildCompoundConstraint());
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
}
