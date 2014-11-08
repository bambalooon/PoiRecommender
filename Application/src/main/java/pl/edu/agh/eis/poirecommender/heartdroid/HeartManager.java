package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import android.util.Log;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import heart.HeaRT;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;
import heart.xtt.State;
import heart.xtt.XTTModel;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.PoiAttributeAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.PoiType;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-08.
 */
public class HeartManager {
    private static final String TAG = HeartManager.class.getSimpleName();
    private static final String RECOMMENDATION_TABLE_NAME = "Recommendations";
    private static final String[] XTT_TABLE_NAMES = new String[] { RECOMMENDATION_TABLE_NAME };
    private static final String POI_ATTRIBUTE_NAME = "poi";
    private HeartPreferences heartPreferences;

    public HeartManager(Context context) {
        this.heartPreferences = new HeartPreferences(context);
    }

    public PoiType inferencePreferredPoiType(List<WithStateElement> stateElements) {
        final XTTModel xttModel = heartPreferences.getXttModel();
        try {
            xttModel.setCurrentState(generateCurrentState(stateElements));
            HeaRT.fixedOrderInference(xttModel, XTT_TABLE_NAMES);
            return adaptPoiTypeFromAttribute(xttModel.getAttributeByName(POI_ATTRIBUTE_NAME));
        } catch (NotInTheDomainException e) {
            throw new IllegalStateException(e);
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage() + "\n" +
                    FluentIterable.from(ImmutableList.copyOf(e.getStackTrace())).join(Joiner.on("\n")));
            return adaptPoiTypeFromAttribute(null);
        }
    }

    private State generateCurrentState(List<WithStateElement> stateElements) {
        final State currentState = new State();
        for (WithStateElement withStateElement : stateElements) {
            currentState.addStateElement(withStateElement.getStateElement());
        }
        return currentState;
    }

    private PoiType adaptPoiTypeFromAttribute(Attribute poiAttribute) {
        return new PoiAttributeAdapter(poiAttribute);
    }
}
