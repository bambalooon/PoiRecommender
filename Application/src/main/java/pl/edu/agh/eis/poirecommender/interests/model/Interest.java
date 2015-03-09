package pl.edu.agh.eis.poirecommender.interests.model;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class Interest {
    private final String name;
    private final int value;

    public Interest(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ": " + value + "%";
    }
}
