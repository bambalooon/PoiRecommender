package pl.edu.agh.eis.poirecommender.application.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.common.collect.ImmutableList;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.debug.AwareFragment;
import pl.edu.agh.eis.poirecommender.application.interests.InterestsFragment;
import pl.edu.agh.eis.poirecommender.application.recommender.RecommenderFragment;
import pl.edu.agh.eis.poirecommender.application.rules.RulesFragment;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    private static final String SAVED_FRAGMENT_TITLE = "SAVED_FRAGMENT_TITLE";
    private static final int STARTUP_ITEM = 0;
    private final List<NavigationDrawerItem> drawerItems = ImmutableList.<NavigationDrawerItem>builder()
            .add(new NavigationDrawerItem(new RecommenderFragment(), R.string.recommendations_fragment, R.drawable.ic_launcher))
            .add(new NavigationDrawerItem(new InterestsFragment(), R.string.interests_fragment, R.drawable.ic_action_interests))
            .add(new NavigationDrawerItem(new RulesFragment(), R.string.rules_fragment, R.drawable.ic_action_rules))
            .add(new NavigationDrawerItem(new AwareFragment(), R.string.aware_fragment, R.drawable.ic_action_debug))
            .build();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private int mTitle;
    private CharSequence mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recommender_toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ToolbarDrawerToggle(toolbar);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);
        mDrawerList.setAdapter(new NavigationDrawerItemsArrayAdapter(this, drawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);

        if (savedInstanceState == null) {
            selectItem(STARTUP_ITEM);
            actionBar.setTitle(mTitle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_FRAGMENT_TITLE, mTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTitle = savedInstanceState.getInt(SAVED_FRAGMENT_TITLE);
        if(!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            getSupportActionBar().setTitle(mTitle);
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
        } else {
            super.onBackPressed();
        }
    }

    private void selectItem(int position) {
        NavigationDrawerItem item = drawerItems.get(position);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_frame, item.getFragment())
                .commit();

        mDrawerList.setItemChecked(position, true);
        mTitle = item.getTitleResource();
        mDrawerLayout.closeDrawer(mDrawerList);
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
            getSupportActionBar().setTitle(mDrawerTitle);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getSupportActionBar().setTitle(mTitle);
            invalidateOptionsMenu();
        }
    }
}
