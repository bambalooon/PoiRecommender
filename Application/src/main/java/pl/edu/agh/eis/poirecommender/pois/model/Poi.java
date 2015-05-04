package pl.edu.agh.eis.poirecommender.pois.model;

import android.location.Location;
import com.aware.poirecommender.openstreetmap.model.response.Element;

/**
 * Name: Poi
 * Description: Poi
 * Date: 2014-11-11
 * Created by BamBalooon
 */

public interface Poi {
    String getName();
    Location getLocation();
    Element getElement();
}
