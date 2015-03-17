package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;

/**
 * Created by Krzysztof Balon on 2014-11-11.
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
    public double getDistance() {
        return currentLocation.distanceTo(getLocation());
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
