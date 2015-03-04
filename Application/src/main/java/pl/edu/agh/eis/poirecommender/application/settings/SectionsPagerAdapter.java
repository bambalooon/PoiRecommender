package pl.edu.agh.eis.poirecommender.application.settings;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import pl.edu.agh.eis.poirecommender.application.Entitled;

import java.util.Locale;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 *
 * @param <T> not existing type extending Fragment and Entitled
 */
public class SectionsPagerAdapter<T extends Fragment & Entitled> extends FragmentPagerAdapter {
    private final Context context;
    private final T[] fragments;

    public SectionsPagerAdapter(FragmentManager fm, Context context, T[] fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(fragments[position].getTitleResource())
                .toUpperCase(Locale.getDefault());
    }
}
