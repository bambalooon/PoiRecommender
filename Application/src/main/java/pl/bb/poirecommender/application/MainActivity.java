package pl.bb.poirecommender.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        initializeTabs();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
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

    private void initializeTabs() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        Locale l = Locale.getDefault();
        String[] titles = {
                getString(R.string.profiles_tab).toUpperCase(l),
                getString(R.string.rules_tab).toUpperCase(l),
                getString(R.string.aware_tab).toUpperCase(l)
        };
        Fragment[] fragments = {
                StaticFragment.instantiate(R.layout.profiles_fragment_main),
                StaticFragment.instantiate(R.layout.rules_fragment_main),
                new AwareFragment()
        };
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles, fragments);
    }
}
