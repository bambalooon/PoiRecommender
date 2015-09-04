package pl.edu.agh.eis.poirecommender.openstreetmap;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.QueryPart;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class PoiTypeToConstraintMapIntegrationTest {

    @Test
    @Parameters(method = "getPoiTypeWithCorrespondingQuery")
    public void shouldGenerateCorrectQueryForPoiType(PoiType poiType, List<String> expectedQuery) {
        //when
        List<String> poiTypeConstraintsQueries = FluentIterable
                .from(PoiTypeToConstraintMap.getConstraint(poiType).getConstraints())
                .transform(QueryPart.TO_STRING)
                .toList();

        //then
        assertEquals(expectedQuery, poiTypeConstraintsQueries);
    }

    @SuppressWarnings("unused")
    private Object[][] getPoiTypeWithCorrespondingQuery() {
        return new Object[][] {
                { PoiType.INDOOR_EATING, Collections
                        .singletonList("[\"amenity\"~\"cafe|fast_food|food_court|ice_cream|pub|restaurant\"]") },
                { PoiType.OUTDOOR_EATING, ImmutableList
                        .of("[\"amenity\"~\"bbq|biergarten|fast_food|ice_cream\"]", "[\"tourism\"=\"picnic_site\"]") },
                { PoiType.DRIVETHROUGH_EATING, Collections.singletonList("[\"takeaway\"=\"yes\"]") },
                { PoiType.INDOOR_SPORTS, ImmutableList.of(
                        "[\"sport\"~\"9pin|10pin|billiards|chess|darts|horse_racing|ice_hockey|ice_skating|sauna|shooting|swimming|table_tennis|table_soccer|tennis\"]",
                        "[\"amenity\"=\"gym\"]") },
                { PoiType.OUTDOOR_SPORTS, Collections.singletonList(
                        "[\"sport\"~\"american_football|badminton|baseball|basketball|beachvolleyball|canoe|cliff_diving|climbing|climbing_adventure|equestrian|fishing|free_flying|golf|karting|kitesurfing|motocross|motor|orienteering|paragliding|sailing|scuba_diving|skiing|soccer|surfing|swimming|tennis\"]") },
                { PoiType.INDOOR_ENTERTAINMENT, ImmutableList.of(
                        "[\"amenity\"~\"brothel|casino|community_centre|gambling|nightclub|planetarium|social_centre|stripclub|swingerclub|stadium\"]",
                        "[\"leisure\"~\"adult_gaming_centre|amusement_arcade|dance|hackerspace|water_park\"]") },
                { PoiType.OUTDOOR_ENTERTAINMENT, ImmutableList.of(
                        "[\"leisure\"~\"beach_resort|bird_hide|firepit|garden|miniature_golf|park|playground|stadium|wildlife_hide\"]",
                        "[\"tourism\"~\"theme_park|viewpoint|zoo\"]") },
                { PoiType.THEATRE_CINEMA, Collections.singletonList(
                        "[\"amenity\"~\"cinema|community_centre|planetarium|theatre\"]") },
                { PoiType.MUSEUM, ImmutableList.of(
                        "[\"amenity\"=\"crypt\"]",
                        "[\"building\"~\"bunker|cathedral|chapel|church|mosque|temple|shrine\"]",
                        "[\"historic\"]",
                        "[\"military\"~\"airfield|bunker|barracks|naval_base\"]",
                        "[\"tourism\"~\"museum|viewpoint\"]") },
                { PoiType.MONUMENTS, ImmutableList.of(
                        "[\"amenity\"=\"crypt\"]",
                        "[\"building\"~\"cathedral|chapel|church|mosque|temple|shrine\"]",
                        "[\"historic\"]",
                        "[\"military\"=\"naval_base\"]") },
                { PoiType.SHOPPING_CENTER, ImmutableList.of(
                        "[\"amenity\"=\"market_place\"]", "[\"shop\"~\"department_store|general|mall|supermarket\"]") }
        };
    }
}