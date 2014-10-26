package pl.bb.poirecommender.application.interests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import pl.bb.poirecommender.application.Entitled;
import pl.bb.poirecommender.application.R;
import pl.bb.poirecommender.application.interests.model.Interest;

import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-10-26.
 */
public class InterestsFragment extends ListFragment implements Entitled {
    private ArrayAdapter<Interest> interestArrayAdapter;
    private SharedPreferences interestPreferences;

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
        interestPreferences = getActivity()
                .getSharedPreferences(Interest.INTEREST_PREFERENCES, Context.MODE_PRIVATE);
        List<Interest> interestList = Interest
                .getInterests(getActivity().getApplicationContext(), R.array.interests, interestPreferences);
        interestArrayAdapter =
                new InterestArrayAdapter(getActivity().getApplicationContext(), interestList, interestPreferences.edit());
        setListAdapter(interestArrayAdapter);
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
