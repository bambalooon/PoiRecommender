package pl.edu.agh.eis.poirecommender.application.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.debug.AwareFragment;
import pl.edu.agh.eis.poirecommender.application.interests.InterestsFragment;
import pl.edu.agh.eis.poirecommender.application.recommender.RecommenderFragment;
import pl.edu.agh.eis.poirecommender.application.rules.RulesFragment;
import pl.edu.agh.eis.poirecommender.aware.AwareContextObservingService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_FRAGMENT_TITLE = "SAVED_FRAGMENT_TITLE";
    private static final String SAVED_SELECTED_FRAGMENT_INDEX = "SAVED_SELECTED_FRAGMENT_INDEX";
    private static final int STARTUP_ITEM = 0;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<NavigationDrawerItem> mDrawerItems;
    private String mFragmentTitle;
    private int mSelectedFragmentIndex = -1;
    private CharSequence mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        mDrawerTitle = getTitle();
        String[] navMenuTitles = getResources().getStringArray(R.array.navigation_drawer_titles);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.navigation_drawer_icons);

        mDrawerItems = new ArrayList<>();
        for (int i=0; i<navMenuTitles.length; i++) {
            mDrawerItems.add(new NavigationDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        navMenuIcons.recycle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recommender_toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ToolbarDrawerToggle(toolbar);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);
        mDrawerList.setAdapter(new NavigationDrawerItemsArrayAdapter(this, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);

        if (savedInstanceState == null) {
            selectItem(STARTUP_ITEM);
            setTitle(mFragmentTitle);
        }

        //TODO: remove service start or move somewhere
        final Intent awareContextObservingService = new Intent(getApplicationContext(), AwareContextObservingService.class);
        Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                startService(awareContextObservingService);
                return null;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_FRAGMENT_TITLE, mFragmentTitle);
        outState.putInt(SAVED_SELECTED_FRAGMENT_INDEX, mSelectedFragmentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFragmentTitle = savedInstanceState.getString(SAVED_FRAGMENT_TITLE);
        mSelectedFragmentIndex = savedInstanceState.getInt(SAVED_SELECTED_FRAGMENT_INDEX);
        if(!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            setTitle(mFragmentTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        for (int i=0; i<menu.size(); i++) {
            menu.getItem(i).setVisible(!isDrawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (mSelectedFragmentIndex > STARTUP_ITEM) {
            selectItem(STARTUP_ITEM);
            setTitle(mFragmentTitle);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    private void selectItem(int position) {
        NavigationDrawerItem item = mDrawerItems.get(position);
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new RecommenderFragment();
                break;
            case 1:
                fragment = new InterestsFragment();
                break;
            case 2:
                fragment = new RulesFragment();
                break;
            case 3:
                fragment = new AwareFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            mFragmentTitle = item.getTitle();
            mSelectedFragmentIndex = position;
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e(TAG, "Error in creating fragment.");
        }
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class ToolbarDrawerToggle extends ActionBarDrawerToggle {
        public ToolbarDrawerToggle(Toolbar toolbar) {
            super(MainActivity.this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            setTitle(mDrawerTitle);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            setTitle(mFragmentTitle);
            invalidateOptionsMenu();
        }
    }
}
