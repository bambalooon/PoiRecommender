package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.xtt.Attribute;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class PoiAttributeAdapter implements PoiType {
    private final Attribute poiAttribute;
    private final String poiTypeName;

    public PoiAttributeAdapter(Attribute poiAttribute) {
        this.poiAttribute = poiAttribute;
        this.poiTypeName = poiAttribute == null || poiAttribute.getValue() == null
                ? null
                : poiAttribute.getValue().toString();
    }

    @Override
    public String getPoiTypeName() {
        return poiTypeName;
    }
}
