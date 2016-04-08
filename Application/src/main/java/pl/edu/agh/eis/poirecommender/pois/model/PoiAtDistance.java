package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.google.common.base.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PoiAtDistance implements Poi, AtDistance, WithEstimatedRating {
    private final RecommendedPoi poi;
    private final Location currentLocation;

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

    @Override
    public double getDistance() {
        return currentLocation.distanceTo(getLocation());
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public Double getEstimatedRating() {
        return poi.getEstimatedRating();
    }

    public static class AttachLocationToPoi implements Function<Poi, PoiAtDistance> {
        private final Location location;

        public AttachLocationToPoi(Location location) {
            this.location = location;
        }

        @Override
        public PoiAtDistance apply(Poi poi) {
            return poi instanceof RecommendedPoi
                    ? new PoiAtDistance((RecommendedPoi) poi, location)
                    : new PoiAtDistance(new RecommendedPoi(poi, null), location);
        }
    }
}
