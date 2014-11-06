package pl.edu.agh.eis.poirecommender.interests;

import com.google.common.collect.ImmutableMap;
import pl.edu.agh.eis.poirecommender.R;

import java.util.Map;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class InterestResources {
    private static final String INTEREST_SPORT = "sport";
    private static final String INTEREST_SHOPPING = "shopping";
    private static final String INTEREST_CULTURE = "culture";
    private static final String INTEREST_SIGHTSEEING = "sightseeing";
    private static final String INTEREST_EATING = "eating";
    private static final String INTEREST_ENTERTAINMENT = "entertainment";
    public static final Map<String, Integer> INTERESTS_MAP = ImmutableMap.<String, Integer>builder()
            .put(INTEREST_SPORT, R.string.sport)
            .put(INTEREST_SHOPPING, R.string.shopping)
            .put(INTEREST_CULTURE, R.string.culture)
            .put(INTEREST_SIGHTSEEING, R.string.sightseeing)
            .put(INTEREST_EATING, R.string.eating)
            .put(INTEREST_ENTERTAINMENT, R.string.entertainment)
            .build();
}
