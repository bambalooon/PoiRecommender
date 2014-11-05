package pl.edu.agh.eis.poirecommender.application.interests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.Entitled;
import pl.edu.agh.eis.poirecommender.interests.InterestPreferences;
import pl.edu.agh.eis.poirecommender.interests.model.Interest;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestsFragment extends ListFragment implements Entitled {
    private ArrayAdapter<Interest> interestArrayAdapter;

    public static InterestsFragment newInstance(int title) {
        InterestsFragment fragment = new InterestsFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interestArrayAdapter = InterestArrayAdapter.newInstance(getActivity().getApplicationContext());
        setListAdapter(interestArrayAdapter);
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
