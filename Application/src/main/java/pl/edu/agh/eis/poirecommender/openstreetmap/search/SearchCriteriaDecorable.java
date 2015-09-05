package pl.edu.agh.eis.poirecommender.openstreetmap.search;

public interface SearchCriteriaDecorable {
    Iterable<String> decorate(Iterable<String> searchCriteria);
}
