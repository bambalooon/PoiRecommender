package pl.edu.agh.eis.poirecommender.application.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.rules.RulesFragment;
import pl.edu.agh.eis.poirecommender.debug.AwareFragment;
import pl.edu.agh.eis.poirecommender.application.interests.InterestsFragment;

/**
 * Created by Krzysztof Balon on 2014-10-19.
 */
public class SettingsActivity extends ActionBarActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private final Fragment[] fragments = {
            InterestsFragment.newInstance(R.string.profiles_tab),
            RulesFragment.newInstance(R.string.rules_tab),
            AwareFragment.newInstance(R.string.aware_tab)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeTabs();
    }

    private void initializeTabs() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(), fragments);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(new ActionBar.TabListener() {
                                @Override
                                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                    mViewPager.setCurrentItem(tab.getPosition());
                                }
                                @Override
                                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                }
                                @Override
                                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                }
                            }));
        }
    }
}
