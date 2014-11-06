package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import pl.edu.agh.eis.poirecommender.aware.model.Activity;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class ActivityAdapter extends StateAdapter<Activity> {
    private static final String ACTIVITY_ATTRIBUTE = "activity";
    private static final float CONFIDENCE_MAX = 100;

    public ActivityAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected String getAttributeName() {
        return ACTIVITY_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        return getAdaptee().getActivity();
    }

    @Override
    protected float calculateCertainty() {
        return getAdaptee().getConfidence()/CONFIDENCE_MAX;
    }
}
