package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.aware.context.property.GenericContextProperty;

public class WindAdapter extends AbstractSymbolicStateAdapter<GenericContextProperty> {
    private static final String WIND_ATTRIBUTE = "windy";
    private static final Double WIND_SPEED_INDICATOR = 8.0;

    private final String contextPropertyAttributeName;

    public WindAdapter(GenericContextProperty contextProperty,
                       String contextPropertyAttributeName) {
        super(contextProperty);
        this.contextPropertyAttributeName = contextPropertyAttributeName;
    }

    @Override
    protected String getAttributeName() {
        return WIND_ATTRIBUTE;
    }

    @Override
    public String adaptValue() {
        GenericContextProperty contextProperty = getAdaptee();
        if (contextProperty == null) {
            return null;
        }
        Double windSpeed = (Double) contextProperty.getAttributes().get(contextPropertyAttributeName);

        if (windSpeed == null) {
            return null;
        }
        return windSpeed > WIND_SPEED_INDICATOR ? "true" : "false";
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
