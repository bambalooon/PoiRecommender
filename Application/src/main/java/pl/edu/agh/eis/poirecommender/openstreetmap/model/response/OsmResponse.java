package pl.edu.agh.eis.poirecommender.openstreetmap.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private final List<Element> elements;

    public OsmResponse(String version, String generator, Osm3s osm3s, List<Element> elements) {
        this.version = version;
        this.generator = generator;
        this.osm3s = osm3s;
        this.elements = elements;
    }

    public String getVersion() {
        return version;
    }

    public String getGenerator() {
        return generator;
    }

    public Osm3s getOsm3s() {
        return osm3s;
    }

    public List<Element> getElements() {
        return elements;
    }
}
