package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class SearchCriteriaPolishLettersDecorator implements SearchCriteriaDecorable {
    private final SearchCriteriaDecorable searchCriteriaDecorable;

    SearchCriteriaPolishLettersDecorator(SearchCriteriaDecorable searchCriteriaDecorable) {
        this.searchCriteriaDecorable = searchCriteriaDecorable;
    }

    @Override
    public Iterable<String> getSearchCriteria() {
        return FluentIterable
                .from(searchCriteriaDecorable.getSearchCriteria())
                .transform(ADD_ALTERNATIVE_LETTER_WITH_TAIL)
                .toList();
    }

    private static final Function<String, String> ADD_ALTERNATIVE_LETTER_WITH_TAIL = new Function<String, String>() {
        @Override
        public String apply(String input) {
            StringBuilder stringBuilder = new StringBuilder(input);
            for (char character : input.toCharArray()) {
                switch (character) {
                    case 'a':
                        stringBuilder.append('ą');
                        break;
                    case 'c':
                        stringBuilder.append('ć');
                        break;
                    case 'e':
                        stringBuilder.append('ę');
                        break;
                    case 'l':
                        stringBuilder.append('ł');
                        break;
                    case 'n':
                        stringBuilder.append('ń');
                        break;
                    case 'o':
                        stringBuilder.append('ó');
                        break;
                    case 's':
                        stringBuilder.append('ś');
                        break;
                    case 'z':
                        stringBuilder.append("źż");
                        break;
                    case 'A':
                        stringBuilder.append('Ą');
                        break;
                    case 'C':
                        stringBuilder.append('Ć');
                        break;
                    case 'E':
                        stringBuilder.append('Ę');
                        break;
                    case 'L':
                        stringBuilder.append('Ł');
                        break;
                    case 'N':
                        stringBuilder.append('Ń');
                        break;
                    case 'O':
                        stringBuilder.append('Ó');
                        break;
                    case 'S':
                        stringBuilder.append('Ś');
                        break;
                    case 'Z':
                        stringBuilder.append("ŹŻ");
                        break;
                }
            }
            return stringBuilder.toString();
        }
    };
}
