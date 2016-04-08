package pl.edu.agh.eis.poirecommender.application.sync;

import android.content.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.eis.poirecommender.Config;
import pl.edu.agh.eis.poirecommender.application.main.ApplicationPreferences;
import pl.edu.agh.eis.poirecommender.model.PoiRecommendationDto;
import pl.edu.agh.eis.poirecommender.poi_recommendation.PoiRecommendationSync;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;

@Slf4j
public class RecommendationsSyncTask extends AbstractSyncTask {
    private final PoiRecommendationSync poiRecommendationSync;
    private final Config config;

    public RecommendationsSyncTask(ApplicationPreferences preferences,
                                   SyncResultJoinNotifier syncResultJoinNotifier,
                                   PoiRecommendationSync poiRecommendationSync,
                                   Config config) {
        super(preferences, syncResultJoinNotifier);
        this.poiRecommendationSync = poiRecommendationSync;
        this.config = config;
    }

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

    public static RecommendationsSyncTask create(Context context, SyncResultJoinNotifier syncResultJoinNotifier) {
        return new RecommendationsSyncTask(
                new ApplicationPreferences(context),
                syncResultJoinNotifier,
                new PoiRecommendationSync(context, new PoiManager(context)),
                new Config(context)
        );
    }
}
