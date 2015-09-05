package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchCriteriaSplitterTest {
    @Test
    public void shouldSplitCorrectlySearchCriteria() {
        //given
        SearchCriteriaSplitter searchCriteriaSplitter = new SearchCriteriaSplitter();

        //when
        Iterable<String> splitSearchCriteria = searchCriteriaSplitter.split("Search criteria.");

        //then
        assertEquals(Arrays.asList("S", "e", "a", "r", "c", "h", " ", "c", "r", "i", "t", "e", "r", "i", "a", "."),
                Lists.newArrayList(splitSearchCriteria));
    }
}
