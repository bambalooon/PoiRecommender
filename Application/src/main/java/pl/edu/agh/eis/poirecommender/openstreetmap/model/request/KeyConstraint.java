package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import pl.edu.agh.eis.poirecommender.pois.model.Poi;

public class KeyConstraint implements Constraint {
    private final String key;

    public KeyConstraint(String key) {
        this.key = key;
    }

    @Override
    public String createQueryPart() {
        return String.format("%c%c%s%c%c",
                CONSTRAINT_REQUEST_PART_START,
                CONSTRAINT_VALUE_START,
                key,
                CONSTRAINT_VALUE_END,
                CONSTRAINT_REQUEST_PART_END);
    }

    @Override
    public boolean eval(Poi poi) {
        return poi.getElement().getTags().containsKey(key);
    }
}
