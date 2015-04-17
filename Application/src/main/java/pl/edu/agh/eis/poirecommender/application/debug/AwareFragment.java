package pl.edu.agh.eis.poirecommender.application.debug;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.aware.context.property.GenericContextProperty;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareFragment extends ListFragment {
    private static final int CONTEXT_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ContextAdapter(getActivity()));
        getLoaderManager().initLoader(CONTEXT_LOADER, null,
                new LoaderManager.LoaderCallbacks<List<GenericContextProperty>>() {
                    @Override
                    public Loader<List<GenericContextProperty>> onCreateLoader(int id, Bundle args) {
                        return new ContextLoader(getActivity());
                    }

                    @Override
                    public void onLoadFinished(Loader<List<GenericContextProperty>> loader,
                                               List<GenericContextProperty> data) {
                        ((ContextAdapter)getListAdapter()).swapContextProperties(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<GenericContextProperty>> loader) {
                        ((ContextAdapter)getListAdapter()).swapContextProperties(null);
                    }
                });
    }
}
