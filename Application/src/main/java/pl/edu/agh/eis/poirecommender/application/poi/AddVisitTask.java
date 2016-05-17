package pl.edu.agh.eis.poirecommender.application.poi;

import android.os.AsyncTask;
import android.widget.Toast;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.visit.VisitService;

@Slf4j
@RequiredArgsConstructor
public class AddVisitTask extends AsyncTask<Poi, Void, AddVisitTask.Result> {
    private final PoiFragment poiFragment;

    @Override
    protected void onPreExecute() {
        poiFragment.checkInPoiButton.setEnabled(false);
    }

    @Override
    protected Result doInBackground(Poi... pois) {
        Preconditions.checkArgument(pois.length == 1);
        Poi poi = pois[0];
        try {
            new VisitService(poiFragment.getContext()).visit(poi);
            return Result.SUCCESS;
        } catch (Exception e) {
            log.error("Error while saving POI visit.", e);
            return Result.FAILURE;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        poiFragment.checkInPoiButton.setEnabled(true);
        Toast.makeText(poiFragment.getActivity(),
                result == Result.SUCCESS ? R.string.check_in_success : R.string.check_in_failure,
                Toast.LENGTH_SHORT).show();
    }

    enum Result {
        SUCCESS, FAILURE
    }
}
