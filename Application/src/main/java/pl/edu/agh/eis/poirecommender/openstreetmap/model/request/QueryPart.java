package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.base.Function;

public interface QueryPart {
    String createQueryPart();

    Function<QueryPart, String> TO_STRING = new Function<QueryPart, String>() {
        @Override
        public String apply(QueryPart queryPart) {
            return queryPart.createQueryPart();
        }
    };
}
