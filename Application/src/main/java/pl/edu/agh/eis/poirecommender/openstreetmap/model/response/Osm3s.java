package pl.edu.agh.eis.poirecommender.openstreetmap.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krzysztof Balon on 2014-11-10.
 */
public class Osm3s {
    @SerializedName("timestamp_osm_base")
    private final String timestamp;
    private final String copyright;

    public Osm3s(String timestamp, String copyright) {
        this.timestamp = timestamp;
        this.copyright = copyright;
    }
}
