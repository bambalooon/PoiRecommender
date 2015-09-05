package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class SearchCriteriaCaseInsensitiveDecoratorTest {
    private SearchCriteriaDecorable searchCriteriaDecorator = new SearchCriteriaCaseInsensitiveDecorator();

    @Test
    public void shouldNotModifySearchCriteriaForNonLetterCharacters() {
        //given
        List<String> nonLetterCharacters = asList(
                "_", " ", "|", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "*", ":", "\"");

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.decorate(nonLetterCharacters);

        //then
        assertEquals(nonLetterCharacters, Lists.newArrayList(searchCriteria));
    }

    @Test
    @Parameters(method = "getSearchCriteria")
    public void shouldModifySearchCriteriaForLetterCharacters(List<String> inSearchCriteria,
                                                              List<String> outSearchCriteria) {
        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.decorate(inSearchCriteria);

        //then
        assertEquals(outSearchCriteria, Lists.newArrayList(searchCriteria));
    }

    @SuppressWarnings("unused")
    private Object[][] getSearchCriteria() {
        return new Object[][] {
                { asList("a", "b", "c", "d", "e"), asList("aA", "bB", "cC", "dD", "eE") },
                { asList("a", "0"), asList("aA", "0") },
                { singletonList("aa"), singletonList("aaAA") },
                { singletonList("oó"), singletonList("oóOÓ") },
                { singletonList("a_+x"), singletonList("a_+xA_+X") }
        };
    }
}
