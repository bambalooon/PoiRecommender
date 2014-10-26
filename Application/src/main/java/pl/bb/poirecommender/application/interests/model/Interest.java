package pl.bb.poirecommender.application.interests.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class Interest {
    public static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
    public static List<Interest> getInterests(Context context, int resId, SharedPreferences interestPreferences) {
        final TypedArray interests = context.getResources().obtainTypedArray(resId);
        List<Interest> interestList = new LinkedList<>();
        int i = 0;
        String interestName;
        while((interestName = interests.getString(i)) != null) {
            final Interest interest = new Interest(interestName);
            interest.setActive(interestPreferences.getBoolean(interestName, false));
            interestList.add(interest);
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
