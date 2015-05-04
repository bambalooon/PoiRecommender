package pl.edu.agh.eis.poirecommender.application.recommender;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.ListView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.poi.PoiFragment;

/**
 * Name: RecommenderFragment
 * Description: RecommenderFragment
 * Date: 2014-11-11
 * Created by BamBalooon
 */
public class RecommenderFragment extends ListFragment {
    private PoiArrayAdapter poiArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        poiArrayAdapter = new PoiArrayAdapter(getActivity());
        setListAdapter(poiArrayAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recommender, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                poiArrayAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommender, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        poiArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PoiFragment poiFragment = PoiFragment.newInstance(poiArrayAdapter.getItem(position).getElement());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, poiFragment)
                .addToBackStack(null)
                .commit();
    }
}
