package pl.edu.agh.eis.poirecommender.openstreetmap.model;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class NodeKeyConstraint implements Constraint {
    private final String key;

    public NodeKeyConstraint(String key) {
        this.key = key;
    }

    @Override
    public String createRequestPart() {
        return String.format("%c%c%s%c%c",
                CONSTRAINT_REQUEST_PART_START,
                CONSTRAINT_VALUE_START,
                key,
                CONSTRAINT_VALUE_END,
                CONSTRAINT_REQUEST_PART_END);
    }
}
