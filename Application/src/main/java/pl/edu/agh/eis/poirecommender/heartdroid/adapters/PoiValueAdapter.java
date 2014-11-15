package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.alsvfd.Value;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class PoiValueAdapter implements WithPoiType {
    private final Value poiTypeValue;
    private final PoiType poiType;

    public PoiValueAdapter(Value poiTypeValue) {
        this.poiTypeValue = poiTypeValue;
        String poiTypeText;
        this.poiType = poiTypeValue == null
                || (poiTypeText = poiTypeValue.toString()) == null
                || poiTypeText.equals(Value.NULL)
                ? null
                : PoiType.fromString(poiTypeText);
    }

    @Override
    public PoiType getPoiType() {
        return poiType;
    }
}
