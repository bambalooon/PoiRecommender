package pl.edu.agh.eis.poirecommender.application.main;

import android.support.v4.app.Fragment;

/**
 * Created by BamBalooon on 2015-03-06.
 */
public class NavigationDrawerItem {
    private final Fragment fragment;
    private final int titleResource;
    private final int iconResource;

    public NavigationDrawerItem(Fragment fragment, int titleResource, int iconResource) {
        this.fragment = fragment;
        this.titleResource = titleResource;
        this.iconResource = iconResource;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getTitleResource() {
        return titleResource;
    }

    public int getIconResource() {
        return iconResource;
    }
}
