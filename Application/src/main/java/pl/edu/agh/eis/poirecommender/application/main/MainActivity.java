package pl.edu.agh.eis.poirecommender.application.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
    private static final int STARTUP_ITEM = 0;
    private final List<NavigationDrawerItem> drawerItems = ImmutableList.<NavigationDrawerItem>builder()
            .add(new NavigationDrawerItem(new RecommenderFragment(), R.string.recommendations_fragment, R.drawable.ic_launcher))
            .add(new NavigationDrawerItem(new InterestsFragment(), R.string.interests_fragment, R.drawable.ic_action_interests))
            .add(new NavigationDrawerItem(new RulesFragment(), R.string.rules_fragment, R.drawable.ic_action_rules))
            .add(new NavigationDrawerItem(new AwareFragment(), R.string.aware_fragment, R.drawable.ic_action_debug))
            .build();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new NavigationDrawerItemsArrayAdapter(this, drawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.recommender_toolbar);
        setSupportActionBar(toolbar);

        selectItem(STARTUP_ITEM);
    }

    private void selectItem(int position) {
        NavigationDrawerItem item = drawerItems.get(position);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_frame, item.getFragment())
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(item.getTitleResource());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
