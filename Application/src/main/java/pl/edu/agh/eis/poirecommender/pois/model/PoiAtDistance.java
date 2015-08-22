package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;
import com.google.common.base.Function;

/**
 * Name: PoiAtDistance
 * Description: PoiAtDistance
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class PoiAtDistance implements Poi, AtDistance {
    private final Poi poi;
    private final Location currentLocation;
    public PoiAtDistance(Poi poi, Location location) {
        this.poi = poi;
        this.currentLocation = location;
    }

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

    public static class AttachLocationToPoi implements Function<Poi, PoiAtDistance> {
        private final Location location;

        public AttachLocationToPoi(Location location) {
            this.location = location;
        }

        @Override
        public PoiAtDistance apply(Poi poi) {
            return new PoiAtDistance(poi, location);
        }
    }
}
