package pl.edu.agh.eis.poirecommender.application.interests;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestsFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new InterestArrayAdapter(getActivity()));
    }
}
