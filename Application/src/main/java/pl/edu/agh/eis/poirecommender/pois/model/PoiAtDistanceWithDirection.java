package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;

import static java.lang.Math.abs;
import static pl.edu.agh.eis.poirecommender.pois.model.CardinalDirection.*;

/**
 * Name: PoiAtDistanceWithDirection
 * Description: PoiAtDistanceWithDirection
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class PoiAtDistanceWithDirection implements Poi, AtDistance, WithDirection {
    private static final double INTERCORDINAL_MIN = 0.4245;
    private static final double INTERCORDINAL_MAX = 2.3559;
    private final PoiAtDistance poi;

    public PoiAtDistanceWithDirection(PoiAtDistance poi) {
        this.poi = poi;
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
        return poi.getDistance();
    }

    @Override
    public CardinalDirection getDirection() {
        double x = getLocation().getLongitude() - poi.getCurrentLocation().getLongitude();
        double y = getLocation().getLatitude() - poi.getCurrentLocation().getLatitude();
        if (x == 0) {
            return y >= 0 ? N : S;
        }
        double tg = y / abs(x);
        return x > 0
                ? tg > INTERCORDINAL_MAX ? N
                    : tg >= INTERCORDINAL_MIN ? NE
                    : tg > -INTERCORDINAL_MIN ? E
                    : tg >= -INTERCORDINAL_MAX ? SE
                    : S
                : tg > INTERCORDINAL_MAX ? N
                    : tg >= INTERCORDINAL_MIN ? NW
                    : tg > -INTERCORDINAL_MIN ? W
                    : tg >= -INTERCORDINAL_MAX ? SW
                    : S;
    }
}
