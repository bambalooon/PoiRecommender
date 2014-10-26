package pl.bb.poirecommender.application.interests;

import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import pl.bb.poirecommender.application.Entitled;
import pl.bb.poirecommender.application.R;
import pl.bb.poirecommender.application.interests.model.Interest;

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
        List<Interest> interestList = Interest.getInterests(getActivity().getApplicationContext(), R.array.interests);
        interestArrayAdapter = new InterestArrayAdapter(getActivity().getApplicationContext(), interestList);
        setListAdapter(interestArrayAdapter);
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
