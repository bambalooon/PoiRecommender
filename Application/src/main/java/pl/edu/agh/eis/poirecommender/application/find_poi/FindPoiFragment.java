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
import android.view.*;
import android.widget.ListView;
import android.widget.ProgressBar;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.poi.PoiFragment;
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
    private PoiListLoader mPoiListLoader;
    private PoiListAdapter mPoiListAdapter;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPoiListAdapter = new PoiListAdapter(getActivity());
        setListAdapter(mPoiListAdapter);
        mPoiListLoader = new PoiListLoader(getActivity());
        getLoaderManager().initLoader(POIS_LOADER, null,
                new LoaderManager.LoaderCallbacks<List<PoiAtDistanceWithDirection>>() {
                    @Override
                    public Loader<List<PoiAtDistanceWithDirection>> onCreateLoader(int id, Bundle args) {
                        return mPoiListLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<List<PoiAtDistanceWithDirection>> loader, List<PoiAtDistanceWithDirection> data) {
                        mPoiListAdapter.swapPoiList(data);
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<PoiAtDistanceWithDirection>> loader) {
                        mPoiListAdapter.swapPoiList(null);
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_poi, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);
        return view;
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO: allow to go back to list of found elements
        //FIXME: bug when after search clicking back button twice?
        PoiFragment poiFragment = PoiFragment.newInstance(mPoiListAdapter.getItem(position).getElement());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, poiFragment)
                .addToBackStack(null)
                .commit();
    }

    public void executePoiSearchQuery(String poiName) {
        mProgressBar.setVisibility(View.VISIBLE);
        mPoiListLoader.loadPois(poiName);
    }
}
