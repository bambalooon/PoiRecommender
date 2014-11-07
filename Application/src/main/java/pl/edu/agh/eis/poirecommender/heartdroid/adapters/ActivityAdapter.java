package pl.edu.agh.eis.poirecommender.heartdroid.adapters;

import pl.edu.agh.eis.poirecommender.aware.model.Activity;

/**
 * Created by Krzysztof Balon on 2014-11-06.
 */
public class ActivityAdapter extends AbstractStateAdapter<Activity> {
    private static final String ACTIVITY_ATTRIBUTE = "activity";
    private static final float CONFIDENCE_MAX = 100;
    private static final int NO_CONFIDENCE = 0;

    public ActivityAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected String getAttributeName() {
        return ACTIVITY_ATTRIBUTE;
    }

    @Override
    protected String adaptValue() {
        final Activity activity = getAdaptee();
        return activity != null
                ? activity.getActivityName()
                : null;
    }

    @Override
    protected float calculateCertainty() {
        final Activity activity = getAdaptee();
        return activity != null
                ? activity.getConfidence()/CONFIDENCE_MAX
                : NO_CONFIDENCE;
    }
}
