package pl.edu.agh.eis.poirecommender.interests;

import android.content.Context;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class InterestStorage {
    private static final int DEFAULT_CERTAINTY = 0;
    private final Map<String, Interest> interestMap;
    private List<Interest> interests;

    public static InterestStorage getDefault(Context context) {
        Map<String, Interest> interestMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry : InterestResources.INTERESTS_MAP.entrySet()) {
            String interestName = entry.getKey();
            String interestValue = context.getString(entry.getValue());
            final Interest interest = new Interest(interestName, interestValue);
            interest.setCertainty(DEFAULT_CERTAINTY);
            interestMap.put(interestName, interest);
        }
        return new InterestStorage(interestMap);
    }

    public InterestStorage(Map<String, Interest> interestMap) {
        this.interestMap = interestMap;
    }

    public List<Interest> getInterests() {
        if(interests == null) {
            interests = new ArrayList<>(interestMap.values());
        }
        return interests;
    }

    public void modifyInterest(Interest interest) {
        interestMap.put(interest.getName(), interest);
        interests = null;
    }
}
