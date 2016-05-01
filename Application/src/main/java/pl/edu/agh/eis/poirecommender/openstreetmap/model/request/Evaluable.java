package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import pl.edu.agh.eis.poirecommender.pois.model.Poi;

public interface Evaluable {
    boolean eval(Poi poi);
}
