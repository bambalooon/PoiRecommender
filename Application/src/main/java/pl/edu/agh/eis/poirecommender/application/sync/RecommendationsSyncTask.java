package pl.edu.agh.eis.poirecommender.application.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.eis.poirecommender.Config;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.main.ApplicationPreferences;
import pl.edu.agh.eis.poirecommender.model.PoiRecommendationDto;
import pl.edu.agh.eis.poirecommender.poi_recommendation.PoiRecommendationSync;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecommendationsSyncTask extends AsyncTask<Void, Void, RecommendationsSyncTask.SyncResult> {
    private final Context context;
    private final PoiRecommendationSync poiRecommendationSync;
    private final ApplicationPreferences preferences;
    private final Config config;

    @Override
    protected SyncResult doInBackground(Void... noParams) {
        try {
            ResponseEntity<PoiRecommendationDto[]> response = new RestTemplate()
                    .exchange(
                            config.getPoiRecommenderRecommendationsSyncUrl(),
                            HttpMethod.POST,
                            new HttpEntity<>(createHeaders()),
                            PoiRecommendationDto[].class
                    );

            if (response.getStatusCode() == HttpStatus.OK) {
                poiRecommendationSync.sync(response.getBody());
                return SyncResult.SUCCESS;
            }
        } catch (Exception e) {
            log.error("POI recommendations sync failed: ", e);
        }
        return SyncResult.FAIL;
    }

    private MultiValueMap<String, String> createHeaders() {
        String basicAuthToken = preferences.getAuthToken();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + basicAuthToken);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected void onPostExecute(SyncResult syncResult) {
        Toast.makeText(
                context,
                syncResult == SyncResult.SUCCESS ? R.string.sync_success : R.string.sync_failed,
                Toast.LENGTH_LONG
        ).show();
    }

    enum SyncResult {
        SUCCESS, FAIL
    }

    public static RecommendationsSyncTask create(Context context) {
        return new RecommendationsSyncTask(
                context,
                new PoiRecommendationSync(context, new PoiManager(context)),
                new ApplicationPreferences(context),
                new Config(context)
        );
    }
}
