package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SearchCriteriaFormatterTest {
    private static final String SEARCH_CRITERIA = "SEARCH_CRITERIA";
    private static final String FORMATTED_SEARCH_CRITERIA = "FORMATTED_SEARCH_CRITERIA";

    @Mock
    private SearchCriteriaDecorator searchCriteriaDecoratorMock1;
    @Mock
    private SearchCriteriaDecorator searchCriteriaDecoratorMock2;
    @Mock
    private SearchCriteriaSplitter searchCriteriaSplitterMock;
    @Mock
    private SearchCriteriaJoiner searchCriteriaJoinerMock;

    private SearchCriteriaFormatter searchCriteriaFormatter;

    @Before
    public void setUp() {
        searchCriteriaFormatter = new SearchCriteriaFormatter(
                ImmutableList.of(searchCriteriaDecoratorMock1, searchCriteriaDecoratorMock2),
                searchCriteriaSplitterMock, searchCriteriaJoinerMock);
    }

    @Test
    public void shouldPerformSearchCriteriaFormatingCorrectly() {
        //given
        Iterable<String> splitSearchCriteria = Lists.newArrayList();
        given(searchCriteriaSplitterMock.split(SEARCH_CRITERIA)).willReturn(splitSearchCriteria);

        Iterable<String> decoratedSearchCriteria = Lists.newArrayList();
        given(searchCriteriaDecoratorMock1.decorate(splitSearchCriteria)).willReturn(decoratedSearchCriteria);

        Iterable<String> decoratedSearchCriteria2 = Lists.newArrayList();
        given(searchCriteriaDecoratorMock2.decorate(decoratedSearchCriteria)).willReturn(decoratedSearchCriteria2);

        given(searchCriteriaJoinerMock.join(decoratedSearchCriteria2)).willReturn(FORMATTED_SEARCH_CRITERIA);

        //when
        String formattedSearchCriteria = searchCriteriaFormatter.format(SEARCH_CRITERIA);

        //then
        assertEquals(FORMATTED_SEARCH_CRITERIA, formattedSearchCriteria);
    }
}