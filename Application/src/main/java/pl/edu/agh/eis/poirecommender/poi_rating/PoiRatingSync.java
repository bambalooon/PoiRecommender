package pl.edu.agh.eis.poirecommender.poi_rating;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.agh.eis.poirecommender.dao.PoiRating;
import pl.edu.agh.eis.poirecommender.model.PoiRatingDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class PoiRatingSync {
    private final PoiRatingService poiRatingService;

    public void sync(PoiRatingDto[] externalPoiRatings, PoiRatingDto[] currentPoiRatings) {
        log.info("Current POI ratings: {}", Arrays.toString(currentPoiRatings));
        log.info("External POI ratings: {}", Arrays.toString(externalPoiRatings));

        Map<Long, PoiRatingDto> externalPoiRatingMap = FluentIterable.of(externalPoiRatings)
                .uniqueIndex(PoiRating.GROUP_BY_POI_ID);
        Map<Long, PoiRatingDto> currentPoiRatingMap = FluentIterable.of(currentPoiRatings)
                .uniqueIndex(PoiRating.GROUP_BY_POI_ID);

        addNewPoiRatings(externalPoiRatingMap, currentPoiRatingMap);
        addConflictedPoiRatings(externalPoiRatingMap, currentPoiRatingMap);
    }

    private void addNewPoiRatings(Map<Long, PoiRatingDto> externalPoiRatings, Map<Long, PoiRatingDto> currentPoiRatings) {
        Set<Long> newPoiIds = Sets.difference(externalPoiRatings.keySet(), currentPoiRatings.keySet());

        List<PoiRatingDto> poiRatingsToAdd = new ArrayList<>(newPoiIds.size());
        for (Long poiId : newPoiIds) {
            poiRatingsToAdd.add(externalPoiRatings.get(poiId));
        }

        poiRatingService.addPoiRatings(FluentIterable.from(poiRatingsToAdd)
                .transform(PoiRating.FROM_POI_RATING_DTO)
                .toList());
    }

    private void addConflictedPoiRatings(Map<Long, PoiRatingDto> externalPoiRatings, Map<Long, PoiRatingDto> currentPoiRatings) {
        Set<Long> conflictedPoiIds = Sets.intersection(externalPoiRatings.keySet(), currentPoiRatings.keySet());

        List<PoiRatingDto> poiRatingsToAdd = new ArrayList<>(conflictedPoiIds.size());
        for (Long poiId : conflictedPoiIds) {
            PoiRatingDto externalPoiRating = externalPoiRatings.get(poiId);
            PoiRatingDto currentPoiRating = currentPoiRatings.get(poiId);

            if (externalPoiRating.getTimestamp().after(currentPoiRating.getTimestamp())) {
                poiRatingsToAdd.add(externalPoiRating);
            }
        }

        poiRatingService.addPoiRatings(FluentIterable.from(poiRatingsToAdd)
                .transform(PoiRating.FROM_POI_RATING_DTO)
                .toList());
    }
}
