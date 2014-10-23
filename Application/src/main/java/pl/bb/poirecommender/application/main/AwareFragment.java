package pl.bb.poirecommender.application.main;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import pl.bb.poirecommender.application.R;
import pl.bb.poirecommender.application.aware.AwareContext;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class AwareFragment extends ListFragment implements Entitled {
    private static final String TAG = AwareFragment.class.getName();
    private ArrayAdapter<Object> activityArrayAdapter;

    public static AwareFragment newInstance(int title) {
        AwareFragment awareFragment = new AwareFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        awareFragment.setArguments(args);
        return awareFragment;
    }

    public void update() {
        activityArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        activityArrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.aware_row,
                R.id.aware_row_text,
                AwareContext.getInstance().getAwareNotifications());
        setListAdapter(activityArrayAdapter);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        update();
        AwareContext.getInstance().registerAwareFragment(this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        AwareContext.getInstance().unregisterAwareFragment();
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
