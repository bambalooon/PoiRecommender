package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public interface Area extends QueryPart {
    char AREA_REQUEST_PART_START = '(';
    char AREA_REQUEST_PART_END = ')';
    char PROPERTY_SEPARATOR = ',';
}