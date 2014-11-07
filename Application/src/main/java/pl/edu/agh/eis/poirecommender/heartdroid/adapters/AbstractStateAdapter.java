package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import heart.alsvfd.SimpleSymbolic;
import heart.xtt.StateElement;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 * @param <T> type adapted from
 */
public abstract class AbstractStateAdapter<T> implements StateAdapter {
    private final T adaptee;

    protected AbstractStateAdapter(T adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public StateElement getStateElement() {
        StateElement stateElement = new StateElement();
        stateElement.setAttributeName(getAttributeName());
        SimpleSymbolic value = new SimpleSymbolic(adaptValue(), null, calculateCertainty());
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
