package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class SearchCriteriaPolishLettersDecoratorTest {
    @Mock
    private SearchCriteriaDecorable searchCriteriaDecorableMock;

    @InjectMocks
    private SearchCriteriaPolishLettersDecorator searchCriteriaDecorator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotModifySearchCriteriaForNonLetterCharacters() {
        //given
        List<String> nonLetterCharacters = asList(
                "_", " ", "|", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "*", ":", "\"");
        given(searchCriteriaDecorableMock.getSearchCriteria()).willReturn(nonLetterCharacters);

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.getSearchCriteria();

        //then
        assertEquals(nonLetterCharacters, Lists.newArrayList(searchCriteria));
    }

    @Test
    public void shouldNotModifySearchCriteriaForLetterCharactersWithoutTailAlternative() {
        //given
        List<String> lettersWithoutTailAlternative = asList("b", "B", "d", "F", "i", "X", "ą", "Ę");
        given(searchCriteriaDecorableMock.getSearchCriteria()).willReturn(lettersWithoutTailAlternative);

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.getSearchCriteria();

        //then
        assertEquals(lettersWithoutTailAlternative, Lists.newArrayList(searchCriteria));
    }

    @Test
    @Parameters(method = "getSearchCriteria")
    public void shouldModifySearchCriteriaForLettersWithTailAlternative(List<String> inSearchCriteria,
                                                                        List<String> outSearchCriteria) {
        //given
        given(searchCriteriaDecorableMock.getSearchCriteria()).willReturn(inSearchCriteria);

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.getSearchCriteria();

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