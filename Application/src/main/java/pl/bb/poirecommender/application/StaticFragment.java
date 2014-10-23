package pl.bb.poirecommender.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BamBalooon on 2014-10-19.
 */
public class StaticFragment extends Fragment {
    protected static final String FRAGMENT_LAYOUT = StaticFragment.class.getName()+".FRAGMENT_LAYOUT";

    public static StaticFragment instantiate(int fragmentLayout) {
        StaticFragment fragment = new StaticFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_LAYOUT, fragmentLayout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int fragmentLayout = this.getArguments().getInt(FRAGMENT_LAYOUT);
        return inflater.inflate(fragmentLayout, container, false);
    }
}
