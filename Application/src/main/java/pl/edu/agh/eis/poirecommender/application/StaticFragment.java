package pl.edu.agh.eis.poirecommender.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class StaticFragment extends Fragment implements Entitled {
    private static final String FRAGMENT_LAYOUT = StaticFragment.class.getName()+".FRAGMENT_LAYOUT";

    public static StaticFragment newInstance(int fragmentLayout, int title) {
        StaticFragment fragment = new StaticFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_LAYOUT, fragmentLayout);
        args.putInt(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int fragmentLayout = this.getArguments().getInt(FRAGMENT_LAYOUT);
        return inflater.inflate(fragmentLayout, container, false);
    }

    @Override
    public int getTitleResource() {
        return getArguments().getInt(TITLE);
    }
}
