package pl.edu.agh.eis.poirecommender.application.recommender;

import android.app.Activity;
import android.location.Location;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.transform.ContextPropertySerialization;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.application.AbstractPoiListAdapter;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Collections;

/**
 * Name: PoiListAdapter
 * Description: PoiListAdapter
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class PoiListAdapter extends AbstractPoiListAdapter {
    //FIXME: add loader to load location in other thread
    private final PoiManager poiManager;
    private final LocationHolder locationHolder;

    public PoiListAdapter(Activity activity) {
        super(activity);
        android.content.Context context = activity.getApplicationContext();
        this.locationHolder = new AwareLocationHolder(
                new Context(context.getContentResolver(),
                        new ContextPropertySerialization<>(GenericContextProperty.class)));
        this.poiManager = new PoiManager(context);
        updatePoiList();
    }

    @Override
    public void notifyDataSetChanged() {
        updatePoiList();
        super.notifyDataSetChanged();
    }

    private void updatePoiList() {
        final PoiStorage poiStorage = poiManager.getPoiStorage();
        final Location location = locationHolder.getLocation();
        //TODO: should accept no location - it'd show POIs without distance and direction
        this.poiList = poiStorage == null || location == null
                ? Collections.<PoiAtDistanceWithDirection>emptyList()
                : FluentIterable.from(poiStorage.getPoiList())
                    .transform(new PoiAtDistance.AttachLocationToPoi(location))
                    .transform(PoiAtDistanceWithDirection.ATTACH_DIRECTION_TO_POI)
                    .toSortedList(PoiAtDistanceWithDirection.DISTANCE_COMPARATOR);
    }
}
