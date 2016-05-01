package pl.edu.agh.eis.poirecommender.poi_recommendation;

import android.content.Context;
import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.model.PoiRecommendationDto;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmExecutor;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmJsonRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.IdConstraint;
import pl.edu.agh.eis.poirecommender.pois.PoiManager;
import pl.edu.agh.eis.poirecommender.pois.PoiStorage;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.pois.model.RecommendedPoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PoiRecommendationSync {
    private final Context context;
    private final PoiManager poiManager;

    public void sync(PoiRecommendationDto[] poiRecommendationDtos) {
        log.info("New POI recommendations: {}", Arrays.toString(poiRecommendationDtos));

        ImmutableList<IdConstraint> recommendedPoiIdConstraints = FluentIterable.of(poiRecommendationDtos)
                .transform(POI_RECOMMENDATION_TO_POI_ID)
                .transform(IdConstraint.POI_ID_TO_CONSTRAINT)
                .toList();
        OsmRequest osmRequest = new OsmJsonRequest(recommendedPoiIdConstraints);
        OsmResponse osmResponse = new OsmExecutor().execute(osmRequest, context);
        if (osmResponse != null) {
            PoiStorage recommendedPois = new PoiStorage(
                    createPoisWithEstimatedRating(poiRecommendationDtos, PoiStorage.fromOsmResponse(osmResponse)));
            poiManager.setRecommendedPois(recommendedPois);
        }
    }

    private List<? extends Poi> createPoisWithEstimatedRating(PoiRecommendationDto[] poiRecommendationDtos,
                                                              PoiStorage recommendedPois) {
        Map<Long, PoiRecommendationDto> poiIdToRecommendationMap = Maps
                .uniqueIndex(Arrays.asList(poiRecommendationDtos), POI_RECOMMENDATION_TO_POI_ID);
        List<RecommendedPoi> poisWithEstimatedRating = new ArrayList<>(poiRecommendationDtos.length);
        for (Poi poi : recommendedPois.getPoiList()) {
            Double poiEstimatedRating = poiIdToRecommendationMap.get(poi.getElement().getId()).getPoiEstimatedRating();
            poisWithEstimatedRating.add(new RecommendedPoi(poi, poiEstimatedRating));
        }
        return poisWithEstimatedRating;
    }

    private static final Function<PoiRecommendationDto, Long> POI_RECOMMENDATION_TO_POI_ID = new Function<PoiRecommendationDto, Long>() {
        @Override
        public Long apply(PoiRecommendationDto poiRecommendationDto) {
            return poiRecommendationDto.getPoiId();
        }
    };
}
