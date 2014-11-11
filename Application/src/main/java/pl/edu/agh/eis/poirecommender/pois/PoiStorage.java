package pl.edu.agh.eis.poirecommender.pois;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.aware.model.Location;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.Element;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.response.OsmResponse;
import pl.edu.agh.eis.poirecommender.pois.model.AtDistance;
import pl.edu.agh.eis.poirecommender.pois.model.BasicPoi;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;
import pl.edu.agh.eis.poirecommender.pois.model.PoiAtDistance;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Krzysztof Balon on 2014-11-11.
 */
public class PoiStorage {
    private List<PoiAtDistance> poiList;

    public static PoiStorage fromOsmResponse(OsmResponse osmResponse, Location location) {
        final List<PoiAtDistance> poiList = FluentIterable.from(osmResponse.getElements())
                .transform(OSM_ELEMENT_TO_BASIC_POI)
                .transform(new AttachLocationToPoi(location))
                .filter(HAS_NAME_FILTER)
                .toSortedList(DISTANCE_COMPARATOR);
        return new PoiStorage(poiList);
    }

    public PoiStorage(List<PoiAtDistance> poiList) {
        this.poiList = poiList;
    }

    public List<PoiAtDistance> getPoiList() {
        return poiList;
    }

    private static class AttachLocationToPoi implements Function<BasicPoi, PoiAtDistance> {
        private final Location location;

        private AttachLocationToPoi(Location location) {
            this.location = location;
        }

        @Override
        public PoiAtDistance apply(BasicPoi poi) {
            return new PoiAtDistance(poi, location);
        }
    }
    private static final Function<Element, BasicPoi> OSM_ELEMENT_TO_BASIC_POI = new Function<Element, BasicPoi>() {
        @Override
        public BasicPoi apply(Element element) {
            return BasicPoi.fromOsmElement(element);
        }
    };
    private static final Predicate<Poi> HAS_NAME_FILTER = new Predicate<Poi>() {
        @Override
        public boolean apply(Poi poi) {
            final String poiName = poi.getName();
            return poiName != null && !poiName.trim().isEmpty();
        }
    };
    private static final Comparator<AtDistance> DISTANCE_COMPARATOR = new Comparator<AtDistance>() {
        @Override
        public int compare(AtDistance atDistance, AtDistance atDistance2) {
            double comparison = atDistance.getDistance() - atDistance2.getDistance();
            return comparison > 0
                    ? 1
                    : comparison == 0
                        ? 0
                        : -1;
        }
    };
}
