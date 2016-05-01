package pl.edu.agh.eis.poirecommender.heartdroid;

import android.content.Context;
import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.xtt.XTTModel;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.PoiValueAdapter;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithPoiType;
import pl.edu.agh.eis.poirecommender.heartdroid.adapters.WithStateElement;

import java.util.List;

public class HeartRuleEngine {
    private static final String[] XTT_TABLE_NAMES = new String[] {"poi"};
    private static final String POI_ATTRIBUTE_NAME = "poi";
    private RulesProvider rulesProvider;

    public HeartRuleEngine(Context context) {
        this.rulesProvider = new RulesProvider(context);
    }

    public WithPoiType inferencePreferredPoiType(List<? extends WithStateElement> stateElements) {
        final XTTModel xttModel = rulesProvider.getXttModel();
        try {
            HeaRT.fixedOrderInference(xttModel, XTT_TABLE_NAMES,
                    new Configuration.Builder()
                            .setUte(new CertaintyFactorsEvaluator())
                            .setInitialState(generateInitialState(stateElements))
                            .build());
            State currentState = HeaRT.getWm().getCurrentState(xttModel);
            return adaptPoiTypeFromAttribute(currentState.getValueOfAttribute(POI_ATTRIBUTE_NAME));
        } catch (NotInTheDomainException | BuilderException | AttributeNotRegisteredException e) {
            throw new IllegalStateException(e);
        }
    }

    private State generateInitialState(List<? extends WithStateElement> stateElements) {
        final State currentState = new State();
        for (WithStateElement withStateElement : stateElements) {
            currentState.addStateElement(withStateElement.getStateElement());
        }
        return currentState;
    }

    private WithPoiType adaptPoiTypeFromAttribute(Value poiValue) {
        return new PoiValueAdapter(poiValue);
    }
}
