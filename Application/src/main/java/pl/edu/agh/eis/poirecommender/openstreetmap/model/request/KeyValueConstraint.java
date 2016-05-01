package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public abstract class KeyValueConstraint implements Constraint {
    private final char assignmentSign;
    protected final String key;
    protected final String value;

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
