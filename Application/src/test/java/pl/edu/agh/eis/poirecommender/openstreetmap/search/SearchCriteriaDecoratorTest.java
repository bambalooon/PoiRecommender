package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchCriteriaDecoratorTest {
    @Test
    public void shouldBuildCorrectDecoratorFromSearchCriteria() {
        //given
        SearchCriteriaDecorator searchCriteriaDecorator = new SearchCriteriaDecorator("Search criteria.");

        //when
        Iterable<String> splitSearchCriteria = searchCriteriaDecorator.getSearchCriteria();

        //then
        assertEquals(Arrays.asList("S", "e", "a", "r", "c", "h", " ", "c", "r", "i", "t", "e", "r", "i", "a", "."),
                Lists.newArrayList(splitSearchCriteria));
    }
}
