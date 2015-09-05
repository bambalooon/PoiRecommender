package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.Iterables;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class SearchCriteriaCaseInsensitiveDecoratorTest {
    @Mock
    private SearchCriteriaDecorable searchCriteriaDecorableMock;
    @InjectMocks
    private SearchCriteriaCaseInsensitiveDecorator searchCriteriaDecorator;

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
        assertTrue(Iterables.elementsEqual(searchCriteria, nonLetterCharacters));
    }

    @Test
    @Parameters(method = "getSearchCriteria")
    public void shouldModifySearchCriteriaForLetterCharacters(List<String> inSearchCriteria,
                                                              List<String> outSearchCriteria) {
        //given
        given(searchCriteriaDecorableMock.getSearchCriteria()).willReturn(inSearchCriteria);

        //when
        Iterable<String> searchCriteria = searchCriteriaDecorator.getSearchCriteria();

        //then
        assertTrue(Iterables.elementsEqual(searchCriteria, outSearchCriteria));
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
