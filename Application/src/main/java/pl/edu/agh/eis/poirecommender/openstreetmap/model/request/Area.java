package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

public interface Area extends QueryPart {
    char AREA_REQUEST_PART_START = '(';
    char AREA_REQUEST_PART_END = ')';
    char PROPERTY_SEPARATOR = ',';
}
