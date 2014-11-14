package pl.edu.agh.eis.poirecommender.pois.model;

import pl.edu.agh.eis.poirecommender.aware.model.Location;

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
    public double getLatitude() {
        return poi.getLatitude();
    }

    @Override
    public double getLongitude() {
        return poi.getLongitude();
    }

    @Override
    public double getDistance() {
        return currentLocation.getDistanceToPointInMeters(poi.getLatitude(), poi.getLongitude());
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
