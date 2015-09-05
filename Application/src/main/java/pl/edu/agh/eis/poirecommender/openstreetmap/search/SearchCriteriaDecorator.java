package pl.edu.agh.eis.poirecommender.openstreetmap.search;

public interface SearchCriteriaDecorator {
    Iterable<String> decorate(Iterable<String> searchCriteria);
}
