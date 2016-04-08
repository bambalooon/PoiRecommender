package pl.edu.agh.eis.poirecommender.application.sync;

import android.content.Context;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.eis.poirecommender.Config;
import pl.edu.agh.eis.poirecommender.application.main.ApplicationPreferences;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.model.PoiRatingDto;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingService;
import pl.edu.agh.eis.poirecommender.poi_rating.PoiRatingSync;
import pl.edu.agh.eis.poirecommender.utils.gson.PoiRatingGsonHttpMessageConverter;

@Slf4j
public class RatingsSyncTask extends AbstractSyncTask {
    private final PoiRatingService poiRatingService;
    private final PoiRatingSync poiRatingSync;
    private final Config config;

    public RatingsSyncTask(ApplicationPreferences preferences,
                           SyncResultJoinNotifier syncResultJoinNotifier,
                           PoiRatingService poiRatingService,
                           PoiRatingSync poiRatingSync,
                           Config config) {
        super(preferences, syncResultJoinNotifier);
        this.poiRatingService = poiRatingService;
        this.poiRatingSync = poiRatingSync;
        this.config = config;
    }

    @Override
    protected SyncResult doInBackground(Void... noParams) {
        try {
            PoiRatingDto[] currentPoiRatings = getPoiRatings();

            ResponseEntity<PoiRatingDto[]> response = new RestTemplate(ImmutableList
                    .<HttpMessageConverter<?>>of(new PoiRatingGsonHttpMessageConverter()))
                    .exchange(
                            config.getPoiRecommenderRatingsSyncUrl(),
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

    public static RatingsSyncTask create(Context context, SyncResultJoinNotifier syncResultJoinNotifier) {
        PoiRatingService poiRatingService = new PoiRatingService(context);
        return new RatingsSyncTask(
                new ApplicationPreferences(context),
                syncResultJoinNotifier,
                poiRatingService,
                new PoiRatingSync(poiRatingService),
                new Config(context)
        );
    }
}
