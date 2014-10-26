package pl.bb.poirecommender.application.interests.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class Interest {
    public static List<Interest> getInterests(Context context, int resId) {
        final TypedArray interests = context.getResources().obtainTypedArray(resId);
        List<Interest> interestList = new LinkedList<>();
        int i = 0;
        String interestName;
        while((interestName = interests.getString(i)) != null) {
            interestList.add(new Interest(interestName));
            i++;
        }
        return interestList;
    }

    private final String name;
    private boolean active = false;

    public Interest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
