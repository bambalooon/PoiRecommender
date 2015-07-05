package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class KeyValueEqualConstraint extends KeyValueConstraint {
    private static final char ASSIGNMENT_SIGN = '=';

    public KeyValueEqualConstraint(String key, String value) {
        super(ASSIGNMENT_SIGN, key, value);
    }
}
