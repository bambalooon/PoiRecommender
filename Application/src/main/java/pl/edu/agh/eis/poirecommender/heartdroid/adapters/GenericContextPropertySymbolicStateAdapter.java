package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.aware.context.property.GenericContextProperty;

/**
 * Name: GenericContextPropertySymbolicStateAdapter
 * Description: GenericContextPropertySymbolicStateAdapter
 * Date: 2015-03-15
 * Created by BamBalooon
 */
public class GenericContextPropertySymbolicStateAdapter extends AbstractSymbolicStateAdapter<GenericContextProperty> {
    private final String xttAttributeName;
    private final String contextPropertyAttributeName;

    public GenericContextPropertySymbolicStateAdapter(GenericContextProperty contextProperty,
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
    protected String adaptValue() {
        GenericContextProperty contextProperty = getAdaptee();
        if (contextProperty == null) {
            return null;
        }
        return (String) contextProperty.getAttributes().get(contextPropertyAttributeName);
    }

    @Override
    protected float calculateCertainty() {
        return 1;
    }
}
