package pl.edu.agh.eis.poirecommender.aware.model;

/**
 * Created by Krzysztof Balon on 2014-11-07.
 *
 * @param <T> type of object to compare
 */
public interface SignificantlyDifferent<T> {
    boolean isSignificantlyDifferent(T object);
}
