package pl.edu.agh.eis.poirecommender.openstreetmap.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krzysztof Balon on 2014-11-10.
 */
public class Element {
    @SerializedName("type")
    private final String type;
    @SerializedName("id")
    private final long id;
    @SerializedName("lat")
    private final double lat;
    @SerializedName("lon")
    private final double lon;
    @SerializedName("tags")
    private final Tags tags;

    public Element(String type, long id, double lat, double lon, Tags tags) {
        this.type = type;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags;
    }
}
