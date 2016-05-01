package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public interface Constraint extends QueryPart, Evaluable {
    char CONSTRAINT_REQUEST_PART_START = '[';
    char CONSTRAINT_REQUEST_PART_END = ']';
    char CONSTRAINT_VALUE_START = '"';
    char CONSTRAINT_VALUE_END = '"';
}
