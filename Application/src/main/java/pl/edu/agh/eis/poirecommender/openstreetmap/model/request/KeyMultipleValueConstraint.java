package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

import java.util.List;

public class KeyMultipleValueConstraint implements Constraint {
    private static final char ASSIGNMENT_SIGN = '~';
    private final Joiner VALUES_JOINER = Joiner.on('|');
    private final String key;
    private final List<String> values;

    public KeyMultipleValueConstraint(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    @Override
    public String createQueryPart() {
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

    @Override
    public boolean eval(Poi poi) {
        String tagValue = poi.getElement().getTags().get(key);
        return tagValue != null && values.contains(tagValue);
    }
}
