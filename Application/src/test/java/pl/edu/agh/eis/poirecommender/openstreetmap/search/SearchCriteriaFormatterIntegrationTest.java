package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchCriteriaFormatterIntegrationTest {
    @Test
    public void shouldFormatSearchCriteria() {
        //when
        String formattedSearchCriteria = new SearchCriteriaFormatter().format("Korona 64");

        //then
        assertEquals("[Kk][oOóÓ][rR][oOóÓ][nNńŃ][aAąĄ] 64", formattedSearchCriteria);
    }
}