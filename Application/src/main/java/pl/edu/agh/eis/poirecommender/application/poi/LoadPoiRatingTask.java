package pl.edu.agh.eis.poirecommender.application.poi;

import android.os.AsyncTask;
import android.view.View;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingService;
import pl.edu.agh.eis.poirecommender.recommendation_entity.Rating;

@RequiredArgsConstructor
public class LoadPoiRatingTask extends AsyncTask<Long, Void, Rating> {
    private final PoiFragment poiFragment;

    @Override
    protected Rating doInBackground(Long... params) {
        long poiId = params[0];
        PoiRating poiRating = new PoiRatingService(poiFragment.getContext()).getPoiRating(poiId);
        return poiRating != null ? poiRating.getPoiRating() : Rating.NONE;
    }

    @Override
    protected void onPostExecute(Rating rating) {
        if (rating != Rating.NONE) {
            poiFragment.ratingBarDecorator.setRating(rating);
            poiFragment.saveRatingButton.setVisibility(View.INVISIBLE);
            poiFragment.removeRatingButton.setVisibility(View.VISIBLE);
        } else { //No rating for selected POI
            poiFragment.saveRatingButton.setVisibility(View.VISIBLE);
            poiFragment.removeRatingButton.setVisibility(View.INVISIBLE);
        }
        poiFragment.progressBar.setVisibility(View.INVISIBLE);
        poiFragment.ratingBarDecorator.enable();
    }
}
