package pl.edu.agh.eis.poirecommender.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    private Long _id;
    private String dayPart;
    private String weekDay;
    private String temperature;
    private String windy;
    private String rain;
    private PoiType poiType;
}
