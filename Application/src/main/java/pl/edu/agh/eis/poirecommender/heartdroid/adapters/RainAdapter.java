package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.aware.context.property.GenericContextProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

import java.util.Map;

public class RainAdapter extends AbstractSymbolicStateAdapter<GenericContextProperty> {
    private static final String RAIN_ATTRIBUTE = "rain";
    private static final Map<Range<Double>, String> RAIN_CLASSIFICATION = ImmutableMap.
            <Range<Double>, String>builder()
            .put(Range.atMost(0.0), "none")
            .put(Range.open(0.0, 4.0), "light")
            .put(Range.atLeast(4.0), "heavy")
            .build();

    private final String contextPropertyAttributeName;

    public RainAdapter(GenericContextProperty contextProperty,
                          String contextPropertyAttributeName) {
        super(contextProperty);
        this.contextPropertyAttributeName = contextPropertyAttributeName;
    }

    @Override
    protected String getAttributeName() {
        return RAIN_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        GenericContextProperty contextProperty = getAdaptee();
        if (contextProperty == null) {
            return null;
        }
        Double rainValue = (Double) contextProperty.getAttributes().get(contextPropertyAttributeName);

        for (Range<Double> rainRange : RAIN_CLASSIFICATION.keySet()) {
            if (rainRange.contains(rainValue)) {
                return RAIN_CLASSIFICATION.get(rainRange);
            }
        }
        return null;
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
