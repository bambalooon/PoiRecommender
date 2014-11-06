package pl.edu.agh.eis.poirecommender.interests;

import android.content.Context;
import android.content.SharedPreferences;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.*;

import static java.util.Map.Entry;

/**
 * Created by Krzysztof Balon on 2014-11-05.
 */
public class InterestPreferences {
    private static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
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
        for(Entry<String, Integer> entry : InterestResources.INTERESTS_MAP.entrySet()) {
            String interestName = entry.getKey();
            String interestValue = context.getString(entry.getValue());
            final Interest interest = new Interest(interestName, interestValue);
            final int certainty = interestPreferences.getInt(interestName, DEFAULT_CERTAINTY);
            interest.setCertainty(certainty);
            interests.add(interest);
        }
    }
}
