package pl.edu.agh.eis.poirecommender.openstreetmap.model;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class KeyValueConstraint implements Constraint {
    private static final char ASSIGNMENT_SIGN = '=';
    private final String key;
    private final String value;

    public KeyValueConstraint(String key, String value) {
        this.key = key;
        this.value = value;
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
                value,
                CONSTRAINT_VALUE_END,
                CONSTRAINT_REQUEST_PART_END);
    }
}
