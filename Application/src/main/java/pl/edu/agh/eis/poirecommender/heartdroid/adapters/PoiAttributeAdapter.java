package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.alsvfd.Value;
import heart.xtt.Attribute;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class PoiAttributeAdapter implements WithPoiType {
    private final Attribute poiAttribute;
    private final PoiType poiType;

    public PoiAttributeAdapter(Attribute poiAttribute) {
        this.poiAttribute = poiAttribute;
        String poiTypeText;
        Value poiTypeValue;
        this.poiType = poiAttribute == null
                || (poiTypeValue = poiAttribute.getValue()) == null
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
