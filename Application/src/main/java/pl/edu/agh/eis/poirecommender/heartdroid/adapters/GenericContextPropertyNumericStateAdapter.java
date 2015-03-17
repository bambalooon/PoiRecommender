package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.aware.context.property.GenericContextProperty;

/**
 * Name: GenericContextPropertyNumericStateAdapter
 * Description: GenericContextPropertyNumericStateAdapter
 * Date: 2015-03-15
 * Created by BamBalooon
 */
public class GenericContextPropertyNumericStateAdapter extends AbstractNumericStateAdapter<GenericContextProperty> {
    private final String xttAttributeName;
    private final String contextPropertyAttributeName;

    public GenericContextPropertyNumericStateAdapter(GenericContextProperty contextProperty,
                                                     String xttAttributeName,
                                                     String contextPropertyAttributeName) {

        super(contextProperty);
        this.xttAttributeName = xttAttributeName;
        this.contextPropertyAttributeName = contextPropertyAttributeName;
    }

    @Override
    protected String getAttributeName() {
        return xttAttributeName;
    }

    @Override
    protected Double adaptValue() {
        GenericContextProperty contextProperty = getAdaptee();
        if (contextProperty == null) {
            return null;
        }
        return (Double) contextProperty.getAttributes().get(contextPropertyAttributeName);
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
