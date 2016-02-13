package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.app.Activity;
import pl.edu.agh.eis.poirecommender.application.AbstractPoiListAdapter;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.AsyncResult;

import java.util.List;

/**
 * Name: PoiListAdapter
 * Description: PoiListAdapter
 * Date: 2015-07-05
 * Created by BamBalooon
 */
public class PoiListAdapter extends AbstractPoiListAdapter {
    public PoiListAdapter(Activity activity) {
        super(activity);
    }

    public void swapPoiList(AsyncResult<? extends List<PoiAtDistanceWithDirection>> newPoiList) {
        poiList = newPoiList;
        if (poiList != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }
}
