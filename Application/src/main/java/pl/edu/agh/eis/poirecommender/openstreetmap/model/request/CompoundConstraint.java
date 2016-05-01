package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

import java.util.List;

public class CompoundConstraint implements Constraint {
    private static final Joiner CONSTRAINTS_AND_JOINER = Joiner.on("");
    private final List<Constraint> constraints;

    public CompoundConstraint(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String createQueryPart() {
        return FluentIterable.from(constraints)
                .transform(new Function<Constraint, String>() {
                    @Override
                    public String apply(Constraint constraint) {
                        return constraint.createQueryPart();
                    }
                }).join(CONSTRAINTS_AND_JOINER);
    }

    @Override
    public boolean eval(Poi poi) {
        for (Constraint constraint : constraints) {
            if (!constraint.eval(poi)) {
                return false;
            }
        }
        return true;
    }
}
