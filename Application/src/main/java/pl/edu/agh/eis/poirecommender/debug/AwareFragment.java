package pl.edu.agh.eis.poirecommender.debug;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.Entitled;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareFragment extends ListFragment implements Entitled {
    private ArrayAdapter<Object> notificationArrayAdapter;

    public static AwareFragment newInstance(int title) {
        AwareFragment awareFragment = new AwareFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        awareFragment.setArguments(args);
        return awareFragment;
    }

    public void update() {
        notificationArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationArrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.aware_row,
                R.id.aware_row_text,
                AwareDebugContext.getInstance().getAwareNotifications());
        setListAdapter(notificationArrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        AwareDebugContext.getInstance().registerAwareFragment(this);
        getListView().setStackFromBottom(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        AwareDebugContext.getInstance().unregisterAwareFragment();
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
