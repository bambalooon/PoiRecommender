package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;

public class SearchCriteriaCaseInsensitiveDecorator implements SearchCriteriaDecorable {
    private final SearchCriteriaDecorable searchCriteriaDecorable;

    SearchCriteriaCaseInsensitiveDecorator(SearchCriteriaDecorable searchCriteriaDecorable) {
        this.searchCriteriaDecorable = searchCriteriaDecorable;
    }

    @Override
    public Iterable<String> getSearchCriteria() {
        return FluentIterable.from(searchCriteriaDecorable.getSearchCriteria())
                .transform(new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        String swappedCase = StringUtils.swapCase(input);
                        return input.equals(swappedCase) ? input : input + swappedCase;
                    }
                }).toList();
    }
}
