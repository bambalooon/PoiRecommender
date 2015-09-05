package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class SearchCriteriaFormatter {
    private final List<SearchCriteriaDecorator> searchCriteriaDecorators;
    private final SearchCriteriaSplitter searchCriteriaSplitter;
    private final SearchCriteriaJoiner searchCriteriaJoiner;

    public SearchCriteriaFormatter() {
        this(ImmutableList.of(new SearchCriteriaCaseInsensitiveDecorator(), new SearchCriteriaPolishLettersDecorator()),
                new SearchCriteriaSplitter(), new SearchCriteriaJoiner());
    }

    SearchCriteriaFormatter(List<SearchCriteriaDecorator> searchCriteriaDecorators,
                                   SearchCriteriaSplitter searchCriteriaSplitter,
                                   SearchCriteriaJoiner searchCriteriaJoiner) {
        this.searchCriteriaDecorators = searchCriteriaDecorators;
        this.searchCriteriaSplitter = searchCriteriaSplitter;
        this.searchCriteriaJoiner = searchCriteriaJoiner;
    }

    public String format(String searchCriteria) {
        Iterable<String> decoratedSearchCriteria = searchCriteriaSplitter.split(searchCriteria);
        for (SearchCriteriaDecorator searchCriteriaDecorator : searchCriteriaDecorators) {
            decoratedSearchCriteria = searchCriteriaDecorator.decorate(decoratedSearchCriteria);
        }
        return searchCriteriaJoiner.join(decoratedSearchCriteria);
    }
}
