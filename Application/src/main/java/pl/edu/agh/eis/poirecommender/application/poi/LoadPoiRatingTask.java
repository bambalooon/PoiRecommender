package pl.edu.agh.eis.poirecommender.application.poi;

import android.os.AsyncTask;
import android.view.View;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingService;

@RequiredArgsConstructor
public class LoadPoiRatingTask extends AsyncTask<Long, Void, Double> {
    private final PoiFragment poiFragment;

    @Override
    protected Double doInBackground(Long... params) {
        long poiId = params[0];
        PoiRating poiRating = new PoiRatingService(poiFragment.getContext()).getPoiRating(poiId);
        return poiRating != null ? poiRating.getPoiRating() : null;
    }

    @Override
    protected void onPostExecute(Double rating) {
        if (rating != null) {
            poiFragment.ratingBarDecorator.setRating(rating); //FIXME: change to enum?
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
