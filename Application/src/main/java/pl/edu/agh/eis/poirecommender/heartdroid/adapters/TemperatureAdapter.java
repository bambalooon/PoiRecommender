package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.aware.context.property.GenericContextProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

import java.util.Map;

public class TemperatureAdapter extends AbstractSymbolicStateAdapter<GenericContextProperty> {
    private static final String TEMPERATURE_ATTRIBUTE = "temperature";
    private static final Map<Range<Double>, String> TEMPERATURE_CLASSIFICATION = ImmutableMap.
            <Range<Double>, String>builder()
            .put(Range.lessThan(4.0), "cold")
            .put(Range.closedOpen(4.0, 15.0), "cool")
            .put(Range.closedOpen(15.0, 24.0), "mild")
            .put(Range.atLeast(24.0), "hot")
            .build();

    private final String contextPropertyAttributeName;

    public TemperatureAdapter(GenericContextProperty contextProperty,
                                 String contextPropertyAttributeName) {
        super(contextProperty);
        this.contextPropertyAttributeName = contextPropertyAttributeName;
    }

    @Override
    protected String getAttributeName() {
        return TEMPERATURE_ATTRIBUTE;
    }

    @Override
    public String adaptValue() {
        GenericContextProperty contextProperty = getAdaptee();
        if (contextProperty == null) {
            return null;
        }
        Double temperatureValue = (Double) contextProperty.getAttributes().get(contextPropertyAttributeName);

        for (Range<Double> rainRange : TEMPERATURE_CLASSIFICATION.keySet()) {
            if (rainRange.contains(temperatureValue)) {
                return TEMPERATURE_CLASSIFICATION.get(rainRange);
            }
        }
        return null;
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
