package pl.edu.agh.eis.poirecommender.application.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.eis.poirecommender.Config;
import pl.edu.agh.eis.poirecommender.R;
import pl.edu.agh.eis.poirecommender.application.main.ApplicationPreferences;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.model.PoiRatingDto;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingService;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingSync;
import pl.edu.agh.eis.poirecommender.utils.gson.PoiRatingGsonHttpMessageConverter;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SyncTask extends AsyncTask<Void, Void, SyncTask.SyncResult> {
    private final Context context;
    private final PoiRatingService poiRatingService;
    private final PoiRatingSync poiRatingSync;
    private final ApplicationPreferences preferences;
    private final Config config;

    @Override
    protected SyncResult doInBackground(Void... noParams) {
        try {
            PoiRatingDto[] currentPoiRatings = getPoiRatings();

            ResponseEntity<PoiRatingDto[]> response = new RestTemplate(ImmutableList
                    .<HttpMessageConverter<?>>of(new PoiRatingGsonHttpMessageConverter()))
                    .exchange(
                            config.getPoiRecommenderSyncUrl(),
                            HttpMethod.POST,
                            new HttpEntity<>(currentPoiRatings, createHeaders()),
                            PoiRatingDto[].class
                    );

            if (response.getStatusCode() == HttpStatus.OK) {
                poiRatingSync.sync(response.getBody(), currentPoiRatings);
                return SyncResult.SUCCESS;
            }
        } catch (Exception e) {
            log.error("POI ratings sync failed: ", e);
        }
        return SyncResult.FAIL;
    }

    private PoiRatingDto[] getPoiRatings() {
        return FluentIterable.from(poiRatingService.getPoiRatings())
                .transform(PoiRating.TO_POI_RATING_DTO)
                .toArray(PoiRatingDto.class);
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

    public static SyncTask create(Context context) {
        PoiRatingService poiRatingService = new PoiRatingService(context);
        return new SyncTask(
                context,
                poiRatingService,
                new PoiRatingSync(poiRatingService),
                new ApplicationPreferences(context),
                new Config(context)
        );
    }
}
