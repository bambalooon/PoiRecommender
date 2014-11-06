package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class InterestListAdapter extends StateListAdapter<Interest> {
    private static final String ATTRIBUTE_INTEREST = "interest";
    private static final float INTEREST_MAX_CERTAINTY = 100;

    public InterestListAdapter(List<Interest> interests) {
        super(interests);
    }

    @Override
    protected String getAttributeName() {
        return ATTRIBUTE_INTEREST;
    }

    @Override
    protected String adaptValue(Interest interest) {
        return interest.getName();
    }

    @Override
    protected float calculateCertainty(Interest interest) {
        return interest.getCertainty()/INTEREST_MAX_CERTAINTY;
    }
}
