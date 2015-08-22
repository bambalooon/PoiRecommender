package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistanceWithDirection;

import java.util.List;

/**
 * Name: FindPoiFragment
 * Description: FindPoiFragment
 * Date: 2015-07-05
 * Created by BamBalooon
 */
public class FindPoiFragment extends ListFragment {
    //TODO: Try to use SearchFragment from support library leanback-v17 instead
    private static final int POIS_LOADER = 0;
    private PoiListLoader poiListLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new PoiListAdapter(getActivity()));
        poiListLoader = new PoiListLoader(getActivity());
        getLoaderManager().initLoader(POIS_LOADER, null,
                new LoaderManager.LoaderCallbacks<List<PoiAtDistanceWithDirection>>() {
                    @Override
                    public Loader<List<PoiAtDistanceWithDirection>> onCreateLoader(int id, Bundle args) {
                        return poiListLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<List<PoiAtDistanceWithDirection>> loader, List<PoiAtDistanceWithDirection> data) {
                        ((PoiListAdapter) getListAdapter()).swapPoiList(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<PoiAtDistanceWithDirection>> loader) {
                        ((PoiListAdapter) getListAdapter()).swapPoiList(null);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.find_poi, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            FragmentActivity activity = getActivity();
            SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_find_poi).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
            searchView.setIconifiedByDefault(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_find_poi:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().onSearchRequested();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public PoiListLoader getPoiListLoader() {
        return poiListLoader;
    }
}
