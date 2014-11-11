package pl.edu.agh.eis.poirecommender.pois.model;

import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.Element;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class BasicPoi implements Poi {
    private final String name;
    private final double latitude;
    private final double longitude;

    public static BasicPoi fromOsmElement(Element osmElement) {
        return new BasicPoi(
                osmElement.getTags() == null
                        ? null
                        : osmElement.getTags().getName(),
                osmElement.getLat(),
                osmElement.getLon());
    }

    public BasicPoi(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
