package pl.edu.agh.eis.poirecommender.interests;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by BamBalooon on 2015-03-08.
 */
public class InterestStorage {
    public static final String[] INTEREST_NAMES =
            {"sport", "shopping", "culture", "sightseeing", "eating", "entertainment"};
    private static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
    private static final int DEFAULT_CERTAINTY = 0;

    private final SharedPreferences interestPreferences;

    public InterestStorage(Context context) {
        this.interestPreferences = context.getSharedPreferences(INTEREST_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setInterest(int position, int value) {
        interestPreferences.edit()
                .putInt(INTEREST_NAMES[position], value)
                .apply();
    }

    public Interest getInterest(int position) {
        String interestName = INTEREST_NAMES[position];
        return getInterest(interestName);
    }

    private Interest getInterest(String interestName) {
        int value = interestPreferences.getInt(interestName, DEFAULT_CERTAINTY);
        return new Interest(interestName, value);
    }

    public List<Interest> getInterestList() {
        return FluentIterable.from(Arrays.asList(INTEREST_NAMES))
                .transform(new Function<String, Interest>() {
                    @Override
                    public Interest apply(String interestName) {
                        return getInterest(interestName);
                    }
                }).toList();
    }
}
