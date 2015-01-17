package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.StateElement;
import heart.alsvfd.Null;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 * @param <T> type adapted from
 */
public abstract class AbstractSymbolicStateAdapter<T> implements WithStateElement {
    private final T adaptee;

    protected AbstractSymbolicStateAdapter(T adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public StateElement getStateElement() {
        StateElement stateElement = new StateElement();
        stateElement.setAttributeName(getAttributeName());
        Value value = adaptValue() == null
                ? new Null()
                : new SimpleSymbolic(adaptValue(), null, calculateCertainty());
        stateElement.setValue(value);
        return stateElement;
    }

    protected abstract String getAttributeName();

    protected abstract String adaptValue();

    protected abstract float calculateCertainty();

    public T getAdaptee() {
        return adaptee;
    }
}
