package pl.edu.agh.eis.poirecommender.pois.model;

import pl.edu.agh.eis.poirecommender.aware.model.Location;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiAtDistance implements Poi, AtDistance {
    private final BasicPoi poi;
    private final Location location;
    public PoiAtDistance(BasicPoi poi, Location location) {
        this.poi = poi;
        this.location = location;
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
        return Location.degreesToKiloMeters(
                Location.calculateAproxDistance(location.getLatitude(), poi.getLatitude(), location.getLongitude(), poi.getLongitude()));
    }
}
