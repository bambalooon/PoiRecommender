package pl.edu.agh.eis.poirecommender.dao;

import com.google.common.base.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.qbusict.cupboard.annotation.Index;
import pl.edu.agh.eis.poirecommender.model.PoiRatingDto;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiRating {
    private Long _id;
    private Long timestamp;
    @Index(unique = true)
    private Long poiId;
    private Rating poiRating;

    public static final Function<PoiRating, PoiRatingDto> TO_POI_RATING_DTO = new Function<PoiRating, PoiRatingDto>() {
        @Override
        public PoiRatingDto apply(PoiRating poiRating) {
            return new PoiRatingDto(
                    null,
                    new Timestamp(poiRating.getTimestamp()),
                    null,
                    poiRating.getPoiId(),
                    poiRating.getPoiRating()
            );
        }
    };

    public static final Function<PoiRatingDto, PoiRating> FROM_POI_RATING_DTO = new Function<PoiRatingDto, PoiRating>() {
        @Override
        public PoiRating apply(PoiRatingDto poiRatingDto) {
            return new PoiRating(
                    null,
                    poiRatingDto.getTimestamp().getTime(),
                    poiRatingDto.getPoiId(),
                    poiRatingDto.getPoiRating()
            );
        }
    };

    public static final Function<PoiRatingDto, Long> GROUP_BY_POI_ID = new Function<PoiRatingDto, Long>() {
        @Override
        public Long apply(PoiRatingDto poiRatingDto) {
            return poiRatingDto.getPoiId();
        }
    };
}
