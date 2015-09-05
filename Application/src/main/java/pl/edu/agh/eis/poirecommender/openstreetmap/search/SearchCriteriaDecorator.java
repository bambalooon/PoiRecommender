package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.base.Splitter;

class SearchCriteriaDecorator implements SearchCriteriaDecorable {
    private final String searchCriteria;

    SearchCriteriaDecorator(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Iterable<String> decorate(Iterable<String> searchCriteria) {
        return Splitter.fixedLength(1).split(this.searchCriteria);
    }
}
