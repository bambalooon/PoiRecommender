package pl.edu.agh.eis.poirecommender.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.qbusict.cupboard.annotation.Index;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiRating {
    private Long _id;
    private Long timestamp;
    @Index(unique = true)
    private Long poiId;
    private Double poiRating;
}
