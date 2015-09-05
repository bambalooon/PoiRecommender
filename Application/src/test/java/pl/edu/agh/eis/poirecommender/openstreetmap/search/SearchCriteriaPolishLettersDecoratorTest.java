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
public class SearchCriteriaPolishLettersDecoratorTest {
    private SearchCriteriaDecorator searchCriteriaDecorator = new SearchCriteriaPolishLettersDecorator();

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
    public void shouldNotModifySearchCriteriaForLetterCharactersWithoutTailAlternative() {
        //given
        List<String> lettersWithoutTailAlternative = asList("b", "B", "d", "F", "i", "X", "ą", "Ę");

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.decorate(lettersWithoutTailAlternative);

        //then
        assertEquals(lettersWithoutTailAlternative, Lists.newArrayList(searchCriteria));
    }

    @Test
    @Parameters(method = "getSearchCriteria")
    public void shouldModifySearchCriteriaForLettersWithTailAlternative(List<String> inSearchCriteria,
                                                                        List<String> outSearchCriteria) {
        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.decorate(inSearchCriteria);

        //then
        assertEquals(outSearchCriteria, Lists.newArrayList(searchCriteria));
    }

    @SuppressWarnings("unused")
    public Object[][] getSearchCriteria() {
        return new Object[][]{
                { singletonList("a"), singletonList("aą") },
                { singletonList("A"), singletonList("AĄ") },
                { singletonList("aA"), singletonList("aAąĄ") },
                { asList("c", "e", "l", "n", "o", "s", "z"), asList("cć", "eę", "lł", "nń", "oó", "sś", "zźż") },
                { asList("C", "E", "L", "N", "O", "S", "Z"), asList("CĆ", "EĘ", "LŁ", "NŃ", "OÓ", "SŚ", "ZŹŻ") },
                { asList("abcdef", "ghijk", "lMn_12"), asList("abcdefąćę", "ghijk", "lMn_12łń") }
        };
    }
}