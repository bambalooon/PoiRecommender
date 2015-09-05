package pl.edu.agh.eis.poirecommender.openstreetmap.search;

import com.google.common.base.Splitter;

public class SearchCriteriaSplitter {
    public Iterable<String> split(String searchCriteria) {
        return Splitter.fixedLength(1).split(searchCriteria);
    }
}
