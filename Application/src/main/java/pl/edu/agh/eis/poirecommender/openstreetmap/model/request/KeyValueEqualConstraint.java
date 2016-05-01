package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import pl.edu.agh.eis.poirecommender.pois.model.Poi;

public class KeyValueEqualConstraint extends KeyValueConstraint {
    private static final char ASSIGNMENT_SIGN = '=';

    public KeyValueEqualConstraint(String key, String value) {
        super(ASSIGNMENT_SIGN, key, value);
    }

    @Override
    public boolean eval(Poi poi) {
        String tagValue = poi.getElement().getTags().get(key);
        return tagValue != null && tagValue.equals(value);
    }
}
