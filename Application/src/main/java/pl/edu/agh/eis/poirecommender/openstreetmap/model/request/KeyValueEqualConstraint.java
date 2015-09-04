package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public class KeyValueEqualConstraint extends KeyValueConstraint {
    private static final char ASSIGNMENT_SIGN = '=';

    public KeyValueEqualConstraint(String key, String value) {
        super(ASSIGNMENT_SIGN, key, value);
    }
}
