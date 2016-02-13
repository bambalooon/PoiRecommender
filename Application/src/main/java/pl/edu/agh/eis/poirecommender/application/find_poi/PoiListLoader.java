package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.location.Location;
import android.support.v4.content.AsyncTaskLoader;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.transform.ContextPropertySerialization;
import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.aware.AwareLocationHolder;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.KeyValueSimilarConstraint;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;
import pl.edu.agh.eis.poirecommender.utils.AsyncResult;
import pl.edu.agh.eis.poirecommender.utils.LocationHolder;

import java.util.List;

@Slf4j
public class PoiListLoader extends AsyncTaskLoader<AsyncResult<? extends List<PoiAtDistanceWithDirection>>> {
    private AsyncResult<? extends List<PoiAtDistanceWithDirection>> mData;
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
    public AsyncResult<? extends List<PoiAtDistanceWithDirection>> loadInBackground() {
        Location location = locationHolder.getLocation();

        if (location == null) {
            return new AsyncResult<>(Optional.<List<PoiAtDistanceWithDirection>>absent(), Optional.of(R.string.location_absent));
        }

        if (poiName == null) {
            return new AsyncResult<>(Optional.<List<PoiAtDistanceWithDirection>>absent(), Optional.<Integer>absent());
        }

        Constraint poiConstraint = new KeyValueSimilarConstraint("name", poiName);

        try {
            OsmResponse osmResponse = new OsmExecutor().execute(new OsmJsonRequest(poiConstraint, location));

            return new AsyncResult<>(Optional.of(FluentIterable.from(osmResponse.getElements())
                    .transform(OsmPoi.OSM_ELEMENT_TO_POI)
                    .transform(new PoiAtDistance.AttachLocationToPoi(location))
                    .transform(PoiAtDistanceWithDirection.ATTACH_DIRECTION_TO_POI)
                    .toSortedList(PoiAtDistanceWithDirection.DISTANCE_COMPARATOR)),
                    Optional.<Integer>absent());
        } catch (RuntimeException e) {
            log.error("OSM query execution error: ", e);
            return new AsyncResult<>(Optional.<List<PoiAtDistanceWithDirection>>absent(), Optional.of(R.string.error_while_loading_pois));
        }
    }

    @Override
    public void deliverResult(AsyncResult<? extends List<PoiAtDistanceWithDirection>> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        AsyncResult<? extends List<PoiAtDistanceWithDirection>> oldData = mData;
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
    public void onCanceled(AsyncResult<? extends List<PoiAtDistanceWithDirection>> data) {
        releaseResources(data);
    }

    protected void releaseResources(AsyncResult<? extends List<PoiAtDistanceWithDirection>> data) {
        //nothing to do with list
    }

    private void onNewDataDelivered() {
    }
}