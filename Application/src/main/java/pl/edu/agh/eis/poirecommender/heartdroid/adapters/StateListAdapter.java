package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.xtt.StateElement;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 * @param <T> type of list items adapted from
 */
public abstract class StateListAdapter<T> implements StateAdaptable {
    private final List<T> adaptee;

    protected StateListAdapter(List<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public StateElement getStateElement() {
        StateElement stateElement = new StateElement();
        stateElement.setAttributeName(getAttributeName());
        SetValue values = new SetValue();
        values.setValues(FluentIterable.from(adaptee)
                .transform(new Function<T, Value>() {
                    @Override
                    public Value apply(T input) {
                        return new SimpleSymbolic(adaptValue(input), null, calculateCertainty(input));
                    }
                }).toList());
        stateElement.setValue(values);
        return stateElement;
    }

    protected abstract String adaptValue(T adaptee);

    protected abstract float calculateCertainty(T adaptee);

    protected abstract String getAttributeName();
}
