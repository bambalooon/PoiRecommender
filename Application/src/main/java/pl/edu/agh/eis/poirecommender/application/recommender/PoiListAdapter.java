package pl.edu.agh.eis.poirecommender.application.recommender;

import android.app.Activity;
import android.location.Location;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.transform.ContextPropertySerialization;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.AbstractPoiListAdapter;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.pois.model.RecommendedPoi;
import pl.edu.agh.eis.poirecommender.utils.AsyncResult;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.List;

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
        final PoiStorage poiStorage = poiManager.getFilteredRecommendedPois();
        final Location location = locationHolder.getLocation();
        //TODO: should accept no location - it'd show POIs without distance and direction
        if (poiStorage == null) {
            poiList = new AsyncResult<>(Optional.<List<PoiAtDistanceWithDirection>>absent(), Optional.of(R.string.no_pois_for_recommendation));
        } else if (location == null) {
            poiList = new AsyncResult<>(Optional.<List<PoiAtDistanceWithDirection>>absent(), Optional.of(R.string.location_absent));
        } else {
            poiList = new AsyncResult<>(Optional.of(FluentIterable.from(poiStorage.getPoiList())
                    .transform(new PoiAtDistance.AttachLocationToPoi(location))
                    .transform(PoiAtDistanceWithDirection.ATTACH_DIRECTION_TO_POI)
                    .toSortedList(RecommendedPoi.DESC_RATING_COMPARATOR)),
                    Optional.<Integer>absent());
        }
    }
}
