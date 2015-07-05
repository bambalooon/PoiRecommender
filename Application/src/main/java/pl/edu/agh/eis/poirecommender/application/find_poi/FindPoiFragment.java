package pl.edu.agh.eis.poirecommender.application.find_poi;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import pl.edu.agh.eis.poirecommender.R;

/**
 * Name: FindPoiFragment
 * Description: FindPoiFragment
 * Date: 2015-07-05
 * Created by BamBalooon
 */
public class FindPoiFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.find_poi, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_find_poi:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
