package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import pl.edu.agh.eis.poirecommender.pois.model.Poi;

public class KeyValueSimilarConstraint extends KeyValueConstraint {
    private static final char ASSIGNMENT_SIGN = '~';

    public KeyValueSimilarConstraint(String key, String value) {
        super(ASSIGNMENT_SIGN, key, value);
    }

    @Override
    public boolean eval(Poi poi) {
        throw new UnsupportedOperationException("Cannot evaluate POI using KeyValueSimilarConstraint.");
    }
}
