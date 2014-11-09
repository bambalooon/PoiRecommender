package pl.edu.agh.eis.poirecommender.openstreetmap.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krzysztof Balon on 2014-11-10.
 */
public class OsmResponse {
    @SerializedName("version")
    private final String version;
    @SerializedName("generator")
    private final String generator;
    @SerializedName("osm3s")
    private final Osm3s osm3s;
    @SerializedName("elements")
    private final Element[] elements;

    public OsmResponse(String version, String generator, Osm3s osm3s, Element[] elements) {
        this.version = version;
        this.generator = generator;
        this.osm3s = osm3s;
        this.elements = elements;
    }
}
