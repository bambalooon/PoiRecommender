package pl.edu.agh.eis.poirecommender.interests;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.lang.reflect.Type;
import java.util.*;

import static java.util.Map.Entry;

/**
 * Created by Krzysztof Balon on 2014-11-05.
 */
public class InterestPreferences {
    private static final Gson GSON_SERIALIZER = new GsonBuilder().enableComplexMapKeySerialization().create();
    private static final Type SERIALIZED_MAP_TYPE = new TypeToken<HashMap<String, Interest>>(){}.getType();
    private static final String INTEREST_PREFERENCES = "INTEREST_PREFERENCES";
    private static final String INTEREST_PREFERENCE_MAP = "INTEREST_PREFERENCE_MAP";
    private static final int DEFAULT_CERTAINTY = 0;

    private final SharedPreferences interestPreferences;
    private final Context context;

    public InterestPreferences(Context context) {
        this.interestPreferences = context.getSharedPreferences(INTEREST_PREFERENCES, Context.MODE_PRIVATE);
        this.context = context;
    }

    public boolean modifyInterest(Interest interest) {
        final Map<String, Interest> interestMap = getInterestMap();
        interestMap.put(interest.getName(), interest);
        return interestPreferences.edit()
                .putString(INTEREST_PREFERENCE_MAP, GSON_SERIALIZER.toJson(interestMap, SERIALIZED_MAP_TYPE))
                .commit();
    }

    public List<Interest> getInterests() {
        return new ArrayList<>(getInterestMap().values());
    }

    private Map<String, Interest> getInterestMap() {
        final String jsonInterests = interestPreferences.getString(INTEREST_PREFERENCE_MAP, null);
        return jsonInterests == null
                ? initInterests()
                : GSON_SERIALIZER.<HashMap<String, Interest>>fromJson(jsonInterests, SERIALIZED_MAP_TYPE);
    }

    private Map<String, Interest> initInterests() {
        Map<String, Interest> interests = new HashMap<>();
        for(Entry<String, Integer> entry : InterestResources.INTERESTS_MAP.entrySet()) {
            String interestName = entry.getKey();
            String interestValue = context.getString(entry.getValue());
            final Interest interest = new Interest(interestName, interestValue);
            interest.setCertainty(DEFAULT_CERTAINTY);
            interests.put(interestName, interest);
        }
        return interests;
    }
}
