package pl.edu.agh.eis.poirecommender.interests;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-05.
 */
public class InterestPreferences {
    private static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
    private static final int INTERESTS_ARRAY = R.array.interests;
    private static final int DEFAULT_CERTAINTY = 0;
    private SharedPreferences interestPreferences;
    private final List<Interest> interests = new ArrayList<>();

    public InterestPreferences(Context context) {
        this.interestPreferences = context.getSharedPreferences(INTEREST_PREFERENCES, Context.MODE_PRIVATE);
        loadInterests(context);
    }

    public boolean modifyCertainty(Interest interest) {
        return interestPreferences.edit()
                .putInt(interest.getName(), interest.getCertainty())
                .commit();
    }

    public List<Interest> getInterests() {
        return interests;
    }

    private void loadInterests(Context context) {
        final TypedArray interestsArray = context.getResources().obtainTypedArray(INTERESTS_ARRAY);
        int i = 0;
        String interestName;
        while((interestName = interestsArray.getString(i)) != null) {
            final Interest interest = new Interest(interestName);
            final int certainty = interestPreferences.getInt(interestName, DEFAULT_CERTAINTY);
            interest.setCertainty(certainty);
            if(certainty == DEFAULT_CERTAINTY) {
                interestPreferences.edit().putInt(interestName, DEFAULT_CERTAINTY);
            }
            interests.add(interest);
            i++;
        }
    }
}
