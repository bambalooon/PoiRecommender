package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;

class SearchCriteriaCaseInsensitiveDecorator implements SearchCriteriaDecorable {
    @Override
    public Iterable<String> decorate(Iterable<String> searchCriteria) {
        return FluentIterable.from(searchCriteria)
                .transform(new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        String swappedCase = StringUtils.swapCase(input);
                        return input.equals(swappedCase) ? input : input + swappedCase;
                    }
                }).toList();
    }
}
