package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import android.location.Location;
import com.aware.context.property.GenericContextProperty;
import com.aware.providers.Locations_Provider;
import com.google.common.base.Function;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Map;

/**
 * Name: AwareContextLocation
 * Description: AwareContextLocation gets ContextProperty with location from AwareContextStorage
 *              and converts it to Android Location
 * Date: 2015-03-15
 * Created by BamBalooon
 */
public class AwareLocationHolder implements LocationHolder {
    private static final String AWARE_LOCATION_PROPERTY_NAME = Locations_Provider.AUTHORITY;
    private final AwareContextStorage awareContextStorage;

    public AwareLocationHolder(Context context) {
        this.awareContextStorage = new AwareContextStorage(context);
    }

    public Location getLocation() {
        GenericContextProperty contextProperty = awareContextStorage.getContextProperty(AWARE_LOCATION_PROPERTY_NAME);
        return CONTEXT_PROPERTY_TO_LOCATION_TRANSFORM.apply(contextProperty);
    }

    private static final Function<GenericContextProperty, Location> CONTEXT_PROPERTY_TO_LOCATION_TRANSFORM
            = new Function<GenericContextProperty, Location>() {

        @Override
        public Location apply(GenericContextProperty contextProperty) {
            if (contextProperty == null) {
                return null;
            }
            Location location = new Location((String) contextProperty.getAttributes().get("provider"));
            for (Map.Entry<String, Object> contextPropertyEntry : contextProperty.getAttributes().entrySet()) {
                switch (contextPropertyEntry.getKey()) {
                    case "timestamp":
                        location.setTime(Long.parseLong((String) contextPropertyEntry.getValue()));
                        break;
                    case "double_latitude":
                        location.setLatitude((Double) contextPropertyEntry.getValue());
                        break;
                    case "double_longitude":
                        location.setLongitude((Double) contextPropertyEntry.getValue());
                        break;
                    case "double_bearing":
                        location.setBearing(((Double) contextPropertyEntry.getValue()).floatValue());
                        break;
                    case "double_speed":
                        location.setSpeed(((Double) contextPropertyEntry.getValue()).floatValue());
                        break;
                    case "double_altitude":
                        location.setAltitude((Double) contextPropertyEntry.getValue());
                        break;
                    case "accuracy":
                        location.setAccuracy(((Double) contextPropertyEntry.getValue()).floatValue());
                        break;
                }
            }
            return location;
        }
    };
}
