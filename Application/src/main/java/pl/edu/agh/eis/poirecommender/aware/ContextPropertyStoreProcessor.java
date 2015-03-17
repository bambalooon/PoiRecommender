package pl.edu.agh.eis.poirecommender.aware;

import android.content.Context;
import android.util.Log;
import com.aware.context.processor.ContextPropertyProcessor;
import com.aware.context.property.GenericContextProperty;
import pl.edu.agh.eis.poirecommender.application.debug.AwareDebugContext;
import pl.edu.agh.eis.poirecommender.service.RecommenderService;

/**
 * Name: ContextPropertyStoreProcessor
 * Description: ContextPropertyStoreProcessor
 * Date: 2015-03-14
 * Created by BamBalooon
 */
public class ContextPropertyStoreProcessor implements ContextPropertyProcessor<GenericContextProperty> {
    private static final String TAG = ContextPropertyStoreProcessor.class.getSimpleName();
    private final Context context;
    private final AwareContextStorage awareContextStorage;

    public ContextPropertyStoreProcessor(Context context) {
        this.context = context;
        this.awareContextStorage = new AwareContextStorage(context);
    }

    @Override
    public void process(GenericContextProperty contextProperty) {
        awareContextStorage.setContextProperty(contextProperty);
        RecommenderService.notifyRecommender(context);
        AwareDebugContext.getInstance().addAwareNotification(contextProperty);
        Log.d(TAG, contextProperty.toString());
    }
}
