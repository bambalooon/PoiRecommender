package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-09.
 */
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
}
