package pl.edu.agh.eis.poirecommender.application.main;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.Toast;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.auth.AuthActivity;
import pl.edu.agh.eis.poirecommender.application.find_poi.FindPoiFragment;
import pl.edu.agh.eis.poirecommender.aware.AwareContextObservingService;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Slf4j
public class MainActivity extends NavigationDrawerActivity {
    private static final int AUTH_REQUEST = 0;
    private ApplicationPreferences preferences;

    public MainActivity() {
        super(R.layout.activity_recommender);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new ApplicationPreferences(getApplicationContext());
        //TODO: start activity with search action in find POI fragment

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);

        //TODO: remove service start or move somewhere
        final Intent awareContextObservingService = new Intent(getApplicationContext(), AwareContextObservingService.class);
        Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                startService(awareContextObservingService);
                return null;
            }
        });
        log.debug("PoiRecommender::MainActivity started.");

        authenticateIfNecessary();
    }

    private void authenticateIfNecessary() {
        String authToken = preferences.getAuthToken();
        if (authToken == null) {
            startActivityForResult(new Intent(AuthActivity.ACTION_AUTH), AUTH_REQUEST);
        } else {
            log.debug("Authentication not necessary, auth token exists: {}", authToken);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AUTH_REQUEST:
                if (resultCode == RESULT_OK) {
                    String token = data.getStringExtra(AuthActivity.AUTH_TOKEN_EXTRA);
                    preferences.setAuthToken(token);
                    Toast.makeText(this, R.string.auth_success, Toast.LENGTH_LONG).show();
                    log.debug("Authentication completed. Received token: {}", token);
                } else {
                    Toast.makeText(this, R.string.auth_canceled, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String poiName = intent.getStringExtra(SearchManager.QUERY);
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (currentFragment instanceof FindPoiFragment) {
                ((FindPoiFragment) currentFragment).executePoiSearchQuery(poiName);
            }
        }
        log.debug("New intent with action: {}", intent.getAction());
    }
}
