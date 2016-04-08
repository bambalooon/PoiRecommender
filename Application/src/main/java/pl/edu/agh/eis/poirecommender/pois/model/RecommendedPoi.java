package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import lombok.Value;

import java.util.Comparator;

@Value
public class RecommendedPoi implements Poi, WithEstimatedRating {
    private final Poi poi;
    private final Double estimatedRating;

    @Override
    public String getName() {
        return poi.getName();
    }

    @Override
    public Location getLocation() {
        return poi.getLocation();
    }

    @Override
    public Element getElement() {
        return poi.getElement();
    }

    public static final Comparator<WithEstimatedRating> DESC_RATING_COMPARATOR = new Comparator<WithEstimatedRating>() {
        @Override
        public int compare(WithEstimatedRating poiWithEstimatedRating1, WithEstimatedRating poiWithEstimatedRating2) {
            Double estimatedRating1 = poiWithEstimatedRating1.getEstimatedRating();
            Double estimatedRating2 = poiWithEstimatedRating2.getEstimatedRating();

            if (estimatedRating1 == null && estimatedRating2 == null) {
                return 0;
            }
            if (estimatedRating1 == null) {
                return 1;
            }
            if (estimatedRating2 == null) {
                return -1;
            }

            double difference = estimatedRating1 - estimatedRating2;
            return difference > 0
                    ? -1
                    : difference == 0 ? 0 : 1;
        }
    };
}
