package pl.edu.agh.eis.poirecommender.application.debug;

import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import com.aware.context.property.GenericContextProperty;
import com.aware.context.provider.Context;
import com.aware.context.provider.ContextContract;
import com.aware.context.transform.ContextPropertySerialization;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Name: ContextLoader
 * Description: ContextLoader
 * Date: 2015-04-17
 * Created by BamBalooon
 */
public class ContextLoader extends AsyncTaskLoader<List<GenericContextProperty>> {
    private static final Uri CONTEXT_URI = ContextContract.Properties.CONTENT_URI;
    private final Context context;
    private List<GenericContextProperty> mData;
    private ForceLoadContentObserver mObserver;

    public ContextLoader(android.content.Context context) {
        super(context);
        this.context = new Context(getContext().getContentResolver(),
                new ContextPropertySerialization<>(GenericContextProperty.class));
        this.mObserver = new ForceLoadContentObserver();
    }

    @Override
    public List<GenericContextProperty> loadInBackground() {
        return Lists.newArrayList(context.getContextProperties().values());
    }

    @Override
    public void deliverResult(List<GenericContextProperty> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<GenericContextProperty> oldData = mData;
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
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    protected void onAbandon() {
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onCanceled(List<GenericContextProperty> data) {
        releaseResources(data);
    }

    protected void releaseResources(List<GenericContextProperty> data) {
        //nothing to do with list
    }

    private void onNewDataDelivered() {
        getContext().getContentResolver().registerContentObserver(CONTEXT_URI, true, mObserver);
    }
}
