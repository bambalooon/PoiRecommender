package pl.edu.agh.eis.poirecommender.openstreetmap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.*;

import java.util.Map;

import static pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType.*;

public class PoiTypeToConstraintMap {
    public static CompositeConstraint getConstraint(PoiType poiType) {
        return MAP.get(poiType);
    }
    private static final Map<PoiType, CompositeConstraint> MAP = ImmutableMap.<PoiType, CompositeConstraint>builder()
            .put(INDOOR_EATING,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("amenity",
                    ImmutableList.<String>builder().add("cafe", "fast_food", "food_court", "ice_cream", "pub", "restaurant").build())
                ).build())
            .put(OUTDOOR_EATING,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("amenity",
                    ImmutableList.<String>builder().add("bbq", "biergarten", "fast_food", "ice_cream").build())
                ).or(new KeyValueEqualConstraint("tourism", "picnic_site")).build())
            .put(DRIVETHROUGH_EATING,
                    new CompositeConstraint.Builder(new KeyValueEqualConstraint("takeaway", "yes")).build())
            .put(INDOOR_SPORTS,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("sport",
                    ImmutableList.<String>builder().add("9pin", "10pin", "billiards", "chess", "darts", "horse_racing", "ice_hockey", "ice_skating", "sauna", "shooting", "swimming", "table_tennis", "table_soccer", "tennis").build())
                ).or(new KeyValueEqualConstraint("amenity", "gym")).build())
            .put(OUTDOOR_SPORTS,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("sport",
                    ImmutableList.<String>builder().add("american_football", "badminton", "baseball", "basketball", "beachvolleyball", "canoe", "cliff_diving", "climbing", "climbing_adventure", "equestrian", "fishing", "free_flying", "golf", "karting", "kitesurfing", "motocross", "motor", "orienteering", "paragliding", "sailing", "scuba_diving", "skiing", "soccer", "surfing", "swimming", "tennis").build())).build())
            .put(THEATRE_CINEMA,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("amenity",
                    ImmutableList.<String>builder().add("cinema", "community_centre", "planetarium", "theatre").build())).build())
            .put(INDOOR_ENTERTAINMENT,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("amenity",
                    ImmutableList.<String>builder().add("brothel", "casino", "community_centre", "gambling", "nightclub", "planetarium", "social_centre", "stripclub", "swingerclub", "stadium").build())
                ).or(new KeyMultipleValueConstraint("leisure",
                    ImmutableList.<String>builder().add("adult_gaming_centre", "amusement_arcade", "dance", "hackerspace", "water_park").build())).build())
            .put(OUTDOOR_ENTERTAINMENT,
                new CompositeConstraint.Builder(new KeyMultipleValueConstraint("leisure",
                    ImmutableList.<String>builder().add("beach_resort", "bird_hide", "firepit", "garden", "miniature_golf", "park", "playground", "stadium", "wildlife_hide").build())
                ).or(new KeyMultipleValueConstraint("tourism",
                    ImmutableList.<String>builder().add("theme_park", "viewpoint", "zoo").build())).build())
            .put(MONUMENTS,
                new CompositeConstraint.Builder(new KeyValueEqualConstraint("amenity", "crypt")
                ).or(new KeyMultipleValueConstraint("building",
                    ImmutableList.<String>builder().add("cathedral", "chapel", "church", "mosque", "temple", "shrine").build())
                ).or(new KeyConstraint("historic")
                ).or(new KeyValueEqualConstraint("military", "naval_base")).build())
            .put(MUSEUM,
                new CompositeConstraint.Builder(new KeyValueEqualConstraint("amenity", "crypt")
                ).or(new KeyMultipleValueConstraint("building",
                    ImmutableList.<String>builder().add("bunker", "cathedral", "chapel", "church", "mosque", "temple", "shrine").build())
                ).or(new KeyConstraint("historic")
                ).or(new KeyMultipleValueConstraint("military",
                    ImmutableList.<String>builder().add("airfield", "bunker", "barracks", "naval_base").build())
                ).or(new KeyMultipleValueConstraint("tourism",
                    ImmutableList.<String>builder().add("museum", "viewpoint").build())).build())
            .put(SHOPPING_CENTER,
                new CompositeConstraint.Builder(new KeyValueEqualConstraint("amenity", "market_place")
                ).or(new KeyMultipleValueConstraint("shop",
                    ImmutableList.<String>builder().add("department_store", "general", "mall", "supermarket").build())).build()
            ).build();
}
