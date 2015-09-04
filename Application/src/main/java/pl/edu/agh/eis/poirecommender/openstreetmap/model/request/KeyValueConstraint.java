package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public class KeyValueConstraint implements Constraint {
    private final char assignmentSign;
    private final String key;
    private final String value;

    public KeyValueConstraint(char assignmentSign, String key, String value) {
        this.assignmentSign = assignmentSign;
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
                assignmentSign,
                CONSTRAINT_VALUE_START,
                value,
                CONSTRAINT_VALUE_END,
                CONSTRAINT_REQUEST_PART_END);
    }
}
