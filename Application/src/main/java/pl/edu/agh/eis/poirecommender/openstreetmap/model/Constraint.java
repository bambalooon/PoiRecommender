package pl.edu.agh.eis.poirecommender.openstreetmap.model;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public interface Constraint extends RequestPart {
    char CONSTRAINT_REQUEST_PART_START = '[';
    char CONSTRAINT_REQUEST_PART_END = ']';
    char CONSTRAINT_VALUE_START = '"';
    char CONSTRAINT_VALUE_END = '"';
}
