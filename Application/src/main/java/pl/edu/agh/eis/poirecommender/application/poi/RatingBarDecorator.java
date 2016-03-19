package pl.edu.agh.eis.poirecommender.application.poi;

import android.widget.RatingBar;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;

/**
 * Name: RatingBarDecorator
 * Description: RatingBarDecorator
 * Date: 2015-05-10
 * Created by BamBalooon
 */
public class RatingBarDecorator {
    private static final float RATING_BAR_DISABLED_ALPHA = .2f;
    private static final float RATING_BAR_ENABLED_ALPHA = 1f;
    private final RatingBar ratingBar;

    public RatingBarDecorator(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public Rating getRating() {
        return Rating.valueOf((double) (ratingBar.getRating() - 1) / (ratingBar.getNumStars() - 1));
    }

    public void setRating(Rating rating) {
        ratingBar.setRating(rating.getValue().floatValue() * (ratingBar.getNumStars() - 1) + 1);
    }

    public void disable() {
        ratingBar.setIsIndicator(true);
        ratingBar.setAlpha(RATING_BAR_DISABLED_ALPHA);
    }

    public void enable() {
        ratingBar.setIsIndicator(false);
        ratingBar.setAlpha(RATING_BAR_ENABLED_ALPHA);
    }
}
