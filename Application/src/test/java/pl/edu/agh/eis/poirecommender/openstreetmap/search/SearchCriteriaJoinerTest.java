package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchCriteriaJoinerTest {
    private SearchCriteriaJoiner searchCriteriaJoiner = new SearchCriteriaJoiner();

    @Test
    public void shouldJoinSingleLetterUnitsNormally() {
        //given
        Iterable<String> singleLetterUnitsCriteria = Arrays.asList("a", "b", "A", "0", "_", ":", "X");

        //when
        String joinedSearchCriteria = searchCriteriaJoiner.join(singleLetterUnitsCriteria);

        //then
        assertEquals("abA0_:X", joinedSearchCriteria);
    }

    @Test
    public void shouldJoinMultipleLettersUnitsWithGrouping() {
        //given
        Iterable<String> multipleLettersUnitsCriteria = Arrays.asList("aAX:", "bBxA", "A;", "f:", "__", ":.:", "X-X");

        //when
        String joinedSearchCriteria = searchCriteriaJoiner.join(multipleLettersUnitsCriteria);

        //then
        assertEquals("[aAX:][bBxA][A;][f:][__][:.:][X-X]", joinedSearchCriteria);
    }
}