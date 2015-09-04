package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public class KeyValueSimilarConstraint extends KeyValueConstraint {
    private static final char ASSIGNMENT_SIGN = '~';

    public KeyValueSimilarConstraint(String key, String value) {
        super(ASSIGNMENT_SIGN, key, value);
    }
}
