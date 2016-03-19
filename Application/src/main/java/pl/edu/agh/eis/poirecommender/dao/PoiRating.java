package pl.edu.agh.eis.poirecommender.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.qbusict.cupboard.annotation.Index;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiRating {
    private Long _id;
    private Long timestamp;
    @Index(unique = true)
    private Long poiId;
    private Rating poiRating;
}
