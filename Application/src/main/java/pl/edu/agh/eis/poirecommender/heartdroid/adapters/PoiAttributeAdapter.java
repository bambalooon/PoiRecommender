package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.xtt.Attribute;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class PoiAttributeAdapter implements WithPoiType {
    private final Attribute poiAttribute;
    private final PoiType poiType;

    public PoiAttributeAdapter(Attribute poiAttribute) {
        pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType.valueOf("indoor-eating");
        this.poiAttribute = poiAttribute;
        this.poiType = poiAttribute == null || poiAttribute.getValue() == null
                ? null
                : PoiType.fromString(poiAttribute.getValue().toString());
    }

    @Override
    public PoiType getPoiType() {
        return poiType;
    }
}
