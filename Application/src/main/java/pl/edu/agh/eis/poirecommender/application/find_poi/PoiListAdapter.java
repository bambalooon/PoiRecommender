package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.app.Activity;
import pl.edu.agh.eis.poirecommender.application.AbstractPoiListAdapter;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;

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

    public void swapPoiList(List<PoiAtDistanceWithDirection> newPoiList) {
        poiList = newPoiList;
        if (poiList != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }
}
