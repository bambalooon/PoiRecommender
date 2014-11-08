package pl.edu.agh.eis.poirecommender.heartdroid.model;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public enum PoiType {
    INDOOR_EATING("indoor-eating"), OUTDOOR_EATING("outdoor-eating"), DRIVETHROUGH_EATING("drivethrough-eating"),
    INDOOR_SPORTS("indoor-sports"), OUTDOOR_SPORTS("outdoor-sports"), THEATRE_CINEMA("theatre-cinema"),
    MONUMENTS("monuments"), INDOOR_ENTERTAINMENT("indoor-entertainment"), OUTDOOR_ENTERTAINMENT("outdoor-entertainment"),
    MUSEUM("museum"), SHOPPING_CENTER("shopping-center");
    private final String text;

    private PoiType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static PoiType fromString(String text) {
        for (PoiType poiType : values()) {
            if(poiType.getText().equalsIgnoreCase(text)) {
                return poiType;
            }
        }
        throw new IllegalArgumentException("Trying to get PoiType from wrong text: " + text);
    }
}
