package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.StateElement;
import heart.alsvfd.Null;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.Value;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 * @param <T> type adapted from
 */
public abstract class AbstractNumericStateAdapter<T> implements WithStateElement {
    private final T adaptee;

    protected AbstractNumericStateAdapter(T adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public StateElement getStateElement() {
        StateElement stateElement = new StateElement();
        stateElement.setAttributeName(getAttributeName());
        Double adaptedValue = adaptValue();
        Value value = adaptedValue == null
                ? new Null()
                : new SimpleNumeric(adaptedValue, calculateCertainty());
        stateElement.setValue(value);
        return stateElement;
    }

    protected abstract String getAttributeName();

    protected abstract Double adaptValue();

    protected abstract float calculateCertainty();

    public T getAdaptee() {
        return adaptee;
    }
}
