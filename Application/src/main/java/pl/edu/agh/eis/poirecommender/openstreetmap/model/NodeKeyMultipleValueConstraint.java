package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class NodeKeyMultipleValueConstraint implements Constraint {
    private final char ASSIGNMENT_SIGN = '~';
    private final Joiner VALUES_JOINER = Joiner.on('|');
    private final String key;
    private final List<String> values;

    public NodeKeyMultipleValueConstraint(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    @Override
    public String createRequestPart() {
        return String.format("%c%c%s%c%c%c%s%c%c",
                CONSTRAINT_REQUEST_PART_START,
                CONSTRAINT_VALUE_START,
                key,
                CONSTRAINT_VALUE_END,
                ASSIGNMENT_SIGN,
                CONSTRAINT_VALUE_START,
                FluentIterable.from(values).join(VALUES_JOINER),
                CONSTRAINT_VALUE_END,
                CONSTRAINT_REQUEST_PART_END);
    }
}
