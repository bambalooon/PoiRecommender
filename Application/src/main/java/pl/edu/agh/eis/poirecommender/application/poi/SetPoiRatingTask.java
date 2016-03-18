package pl.edu.agh.eis.poirecommender.application.poi;

import android.os.AsyncTask;
import android.view.View;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingService;

@RequiredArgsConstructor
public class SetPoiRatingTask extends AsyncTask<Pair<Long, Double>, Void, SetPoiRatingTask.Result> {
    private final PoiFragment poiFragment;

    @Override
    protected void onPreExecute() {
        poiFragment.saveRatingButton.setVisibility(View.INVISIBLE);
        poiFragment.removeRatingButton.setVisibility(View.INVISIBLE);
        poiFragment.progressBar.setVisibility(View.VISIBLE);
        poiFragment.ratingBarDecorator.disable();
    }

    @Override
    protected Result doInBackground(Pair<Long, Double>... params) {
        Long poiId = params[0].getLeft();
        Double poiRating = params[0].getRight();
        new PoiRatingService(poiFragment.getContext()).ratePoi(poiId, poiRating);
        return poiRating == null ? Result.REMOVED : Result.SET;
    }

    @Override
    protected void onPostExecute(Result result) {
        poiFragment.progressBar.setVisibility(View.INVISIBLE);
        poiFragment.ratingBarDecorator.enable();
        if (result == Result.SET) {
            poiFragment.saveRatingButton.setVisibility(View.INVISIBLE);
            poiFragment.removeRatingButton.setVisibility(View.VISIBLE);
        } else {
            poiFragment.saveRatingButton.setVisibility(View.VISIBLE);
            poiFragment.removeRatingButton.setVisibility(View.INVISIBLE);
        }
    }

    enum Result {
        SET, REMOVED
    }
}
