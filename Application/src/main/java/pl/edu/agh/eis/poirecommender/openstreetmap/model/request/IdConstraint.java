package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.base.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdConstraint implements QueryPart {
    private static char ID_PART_START = '(';
    private static char ID_PART_END = ')';
    private final long id;

    @Override
    public String createQueryPart() {
        return String.format("%c%d%c", ID_PART_START, id, ID_PART_END);
    }

    public static final Function<Long, IdConstraint> POI_ID_TO_CONSTRAINT = new Function<Long, IdConstraint>() {
        @Override
        public IdConstraint apply(Long poiId) {
            return new IdConstraint(poiId);
        }
    };
}
