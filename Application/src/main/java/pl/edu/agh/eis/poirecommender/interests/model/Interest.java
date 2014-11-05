package pl.edu.agh.eis.poirecommender.interests.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class Interest {
    private final String name;
    private int certainty;

    public Interest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCertainty() {
        return certainty;
    }

    public void setCertainty(int certainty) {
        this.certainty = certainty;
    }

    @Override
    public String toString() {
        return name + ": " + certainty + "%";
    }
}
