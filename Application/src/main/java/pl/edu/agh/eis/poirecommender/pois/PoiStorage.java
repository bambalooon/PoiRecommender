package pl.edu.agh.eis.poirecommender.pois;

import com.aware.poirecommender.openstreetmap.model.response.OsmResponse;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import pl.edu.agh.eis.poirecommender.pois.model.Poi;

import java.util.List;

import static pl.edu.agh.eis.poirecommender.pois.model.OsmPoi.OSM_ELEMENT_TO_POI;

public class PoiStorage {
    private final List<? extends Poi> poiList;

    public static PoiStorage fromOsmResponse(OsmResponse osmResponse) {
        final List<? extends Poi> poiList = FluentIterable.from(osmResponse.getElements())
                .transform(OSM_ELEMENT_TO_POI)
                .filter(HAS_NAME_FILTER)
                .toList();
        return new PoiStorage(poiList);
    }

    public PoiStorage(List<? extends Poi> poiList) {
        this.poiList = poiList;
    }

    public List<? extends Poi> getPoiList() {
        return poiList;
    }

    private static final Predicate<Poi> HAS_NAME_FILTER = new Predicate<Poi>() {
        @Override
        public boolean apply(Poi poi) {
            final String poiName = poi.getName();
            return poiName != null && !poiName.trim().isEmpty();
        }
    };
}
