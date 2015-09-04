package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.location.Location;
import android.support.v4.content.AsyncTaskLoader;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.KeyValueSimilarConstraint;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.Collections;
import java.util.List;

public class PoiListLoader extends AsyncTaskLoader<List<PoiAtDistanceWithDirection>> {
    private List<PoiAtDistanceWithDirection> mData;
    private LocationHolder locationHolder;
    private String poiName;

    public PoiListLoader(android.content.Context appContext) {
        super(appContext);
        Context context = new Context(
                appContext.getContentResolver(),
                new ContextPropertySerialization<>(GenericContextProperty.class));
        locationHolder = new AwareLocationHolder(context);
    }

    public void loadPois(String poiName) {
        this.poiName = poiName;
        onContentChanged();
    }

    @Override
    public List<PoiAtDistanceWithDirection> loadInBackground() {
        Location location = locationHolder.getLocation();

        if (poiName == null || location == null) {
            return Collections.emptyList();
        }

        Constraint poiConstraint = new KeyValueSimilarConstraint("name", poiName);

        OsmResponse osmResponse = new OsmExecutor().execute(new OsmJsonRequest(poiConstraint, location));

        return FluentIterable.from(osmResponse.getElements())
                .transform(OsmPoi.OSM_ELEMENT_TO_POI)
                .transform(new PoiAtDistance.AttachLocationToPoi(location))
                .transform(PoiAtDistanceWithDirection.ATTACH_DIRECTION_TO_POI)
                .toSortedList(PoiAtDistanceWithDirection.DISTANCE_COMPARATOR);
    }

    @Override
    public void deliverResult(List<PoiAtDistanceWithDirection> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<PoiAtDistanceWithDirection> oldData = mData;
        mData = data;

        if (isStarted()) {
            if (oldData != data) {
                onNewDataDelivered();
            }
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }
    }

    @Override
    protected void onAbandon() {
    }

    @Override
    public void onCanceled(List<PoiAtDistanceWithDirection> data) {
        releaseResources(data);
    }

    protected void releaseResources(List<PoiAtDistanceWithDirection> data) {
        //nothing to do with list
    }

    private void onNewDataDelivered() {
    }
}