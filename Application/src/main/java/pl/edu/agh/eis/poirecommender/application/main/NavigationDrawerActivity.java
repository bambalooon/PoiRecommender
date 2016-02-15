package pl.edu.agh.eis.poirecommender.application.main;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.debug.AwareFragment;
import pl.edu.agh.eis.poirecommender.application.find_poi.FindPoiFragment;
import pl.edu.agh.eis.poirecommender.application.recommender.RecommenderFragment;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class NavigationDrawerActivity extends AppCompatActivity {
    private final int activityLayout;
    private NavigationDrawer navigationDrawer;
    private ActionBarDrawerToggle drawerToggleButton;
    private CharSequence appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityLayout);

        appTitle = getTitle();

        DrawerLayout drawerLayout = setUpDrawerLayoutAndToolbar();
        List<NavigationDrawerItem> drawerItems = setUpDrawerItems();
        ListView drawerListView = setUpDrawerListView(drawerItems);

        navigationDrawer = new NavigationDrawer(drawerLayout, drawerListView, drawerItems);

        if (savedInstanceState == null) {
            loadStartUpItem();
        }
    }

    private DrawerLayout setUpDrawerLayoutAndToolbar() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerToggleButton = new ToolbarDrawerToggle(drawerLayout, toolbar);
        drawerLayout.setDrawerListener(drawerToggleButton);
        return drawerLayout;
    }

    private List<NavigationDrawerItem> setUpDrawerItems() {
        String[] navMenuTitles = getResources().getStringArray(R.array.navigation_drawer_titles);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.navigation_drawer_icons);
        FragmentFactory[] fragmentFactories = new FragmentFactory[]{
                new FragmentFactory() {
                    @Override
                    public Fragment createFragment() {
                        return new RecommenderFragment();
                    }
                },
                new FragmentFactory() {
                    @Override
                    public Fragment createFragment() {
                        return new FindPoiFragment();
                    }
                },
                new FragmentFactory() {
                    @Override
                    public Fragment createFragment() {
                        return new AwareFragment();
                    }
                }};

        List<NavigationDrawerItem> items = new ArrayList<>();
        for (int i = 0; i < navMenuTitles.length; i++) {
            items.add(new NavigationDrawerItem(
                    navMenuTitles[i], navMenuIcons.getResourceId(i, -1), fragmentFactories[i]));
        }
        navMenuIcons.recycle();

        return items;
    }

    private ListView setUpDrawerListView(List<NavigationDrawerItem> drawerItems) {
        ListView drawerListView = (ListView) findViewById(R.id.navigation_drawer);
        drawerListView.setAdapter(new NavigationDrawerItemsArrayAdapter(this, drawerItems));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        return drawerListView;
    }

    private void loadStartUpItem() {
        navigationDrawer.initCurrentItem();
        loadFragment(navigationDrawer.getCurrentItem().getFragmentFactory().createFragment());
        navigationDrawer.closeDrawer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationDrawer.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationDrawer.restoreState(savedInstanceState);
        if (!navigationDrawer.isDrawerOpen()) {
            setTitle(navigationDrawer.getCurrentItem().getTitle());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggleButton.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggleButton.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen = navigationDrawer.isDrawerOpen();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(!isDrawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
        } else if (isBackStackEmpty() && !navigationDrawer.isStartUpItem()) {
            loadStartUpItem();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isBackStackEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    private void loadFragment(Fragment fragment) {
        log.debug("Loading fragment: {}", fragment.getClass().getSimpleName());
        FragmentManager fm = getSupportFragmentManager();
        clearBackStack(fm);
        fm.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void clearBackStack(FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            navigationDrawer.changeCurrentItem(position);
            loadFragment(navigationDrawer.getCurrentItem().getFragmentFactory().createFragment());
            navigationDrawer.closeDrawer();
        }
    }

    private class ToolbarDrawerToggle extends ActionBarDrawerToggle {
        public ToolbarDrawerToggle(DrawerLayout drawerLayout, Toolbar toolbar) {
            super(NavigationDrawerActivity.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            setTitle(appTitle);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            setTitle(navigationDrawer.getCurrentItem().getTitle());
            invalidateOptionsMenu();
        }
    }
}
