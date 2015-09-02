package pl.edu.agh.eis.poirecommender.openstreetmap.model;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.agh.eis.poirecommender.openstreetmap.OsmXmlRequest;
import pl.edu.agh.eis.poirecommender.openstreetmap.PoiTypeToConstraintMap;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.*;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static pl.edu.agh.eis.poirecommender.heartdroid.model.PoiType.*;

@RunWith(AndroidJUnit4.class)
public class OsmModelTest {
    private static final double EXPECTED_LATITUDE = 10.15161;
    private static final double EXPECTED_LONGITUDE = 20.16171801;
    private static final float EXPECTED_RADIUS = 300.1701f;
    private static final String KEY_CONSTRAINT_KEY = "keyConstraint";
    private static final String KEY_VALUE_CONSTRAINT_KEY = "keyValueConstraintKey";
    private static final String KEY_VALUE_CONSTRAINT_VALUE = "keyValueConstraintValue";
    private static final String KEY_MULTI_VALUE_CONSTRAINT_KEY = "keyMultiValueConstraintKey";
    private static final List<String> KEY_MULTI_VALUE_CONSTRAINT_VALUES = ImmutableList.of(
            "keyMultiValueConstraintValue1", "keyMultiValueConstraintValue2", "keyMultiValueConstraintValue3");

    private Area aroundArea;
    private Constraint keyConstraint;
    private Constraint keyValueConstraint;
    private Constraint keyMultipleValueConstraint;

    @Before
    public void setUp() {
        final Location location = generateLocation(EXPECTED_LATITUDE, EXPECTED_LONGITUDE);
        this.aroundArea = new AroundArea(location, EXPECTED_RADIUS);
        this.keyConstraint = new KeyConstraint(KEY_CONSTRAINT_KEY);
        this.keyValueConstraint = new KeyValueEqualConstraint(KEY_VALUE_CONSTRAINT_KEY, KEY_VALUE_CONSTRAINT_VALUE);
        this.keyMultipleValueConstraint =
                new KeyMultipleValueConstraint(KEY_MULTI_VALUE_CONSTRAINT_KEY, KEY_MULTI_VALUE_CONSTRAINT_VALUES);
    }

    @Test
    public void testAroundAreaRequestPartCreation() {
        //when
        final String createdQueryPart = aroundArea.createQueryPart();

        //then
        assertEquals("(around:300.17,10.151610,20.161718)", createdQueryPart);
    }

    @Test
    public void testNodeKeyConstraintRequestPartCreation() {
        //when
        final String createdQueryPart = keyConstraint.createQueryPart();

        //then
        assertEquals("[\"keyConstraint\"]", createdQueryPart);
    }

    @Test
    public void testNodeKeyValueConstraintRequestPartCreation() {
        //when
        final String createdQueryPart = keyValueConstraint.createQueryPart();

        //then
        assertEquals("[\"keyValueConstraintKey\"=\"keyValueConstraintValue\"]", createdQueryPart);
    }

    @Test
    public void testNodeKeyMultipleValueConstraintRequestPartCreation() {
        //when
        final String createdQueryPart = keyMultipleValueConstraint.createQueryPart();

        //then
        assertEquals(
                "[\"keyMultiValueConstraintKey\"~\"keyMultiValueConstraintValue1|keyMultiValueConstraintValue2|keyMultiValueConstraintValue3\"]",
                createdQueryPart);
    }

    @Test
    public void testNodeRequestPartCreation() {
        //when
        final String createdQuery = new Node(aroundArea, keyConstraint).createQuery();

        //then
        assertEquals(
                "node(around:300.17,10.151610,20.161718)[\"keyConstraint\"]",
                createdQuery);
    }

    @Test
    public void testCompoundConstraint() {
        //given
        List<Constraint> constraints = ImmutableList.of(keyConstraint, keyValueConstraint, keyMultipleValueConstraint);

        //when
        final String createdQueryPart = new CompoundConstraint(constraints).createQueryPart();

        //then
        assertEquals(
                "[\"keyConstraint\"][\"keyValueConstraintKey\"=\"keyValueConstraintValue\"][\"keyMultiValueConstraintKey\"~\"keyMultiValueConstraintValue1|keyMultiValueConstraintValue2|keyMultiValueConstraintValue3\"]",
                createdQueryPart);
    }

    @Test
    public void testCompositeConstraintBuilder() {
        //when
        final CompositeConstraint compositeConstraint = new CompositeConstraintBuilder(keyConstraint).and(keyValueConstraint).and(keyMultipleValueConstraint)
                .or(keyConstraint).and(keyValueConstraint)
                .or(keyMultipleValueConstraint).build();

        //then
        assertEquals(
                "[\"keyConstraint\"][\"keyValueConstraintKey\"=\"keyValueConstraintValue\"][\"keyMultiValueConstraintKey\"~\"keyMultiValueConstraintValue1|keyMultiValueConstraintValue2|keyMultiValueConstraintValue3\"]",
                compositeConstraint.getConstraints().get(0).createQueryPart());
        assertEquals(
                "[\"keyConstraint\"][\"keyValueConstraintKey\"=\"keyValueConstraintValue\"]",
                compositeConstraint.getConstraints().get(1).createQueryPart());
        assertEquals(
                "[\"keyMultiValueConstraintKey\"~\"keyMultiValueConstraintValue1|keyMultiValueConstraintValue2|keyMultiValueConstraintValue3\"]",
                compositeConstraint.getConstraints().get(2).createQueryPart());
    }

    @Test
    public void testPoiTypeToConstraintMapping() {
        //given
        final List<String> expectedIndoorEatingConstraintQueryParts = ImmutableList
                .of("[\"amenity\"~\"cafe|fast_food|food_court|ice_cream|pub|restaurant\"]");
        final List<String> expectedOutdoorEatingConstraintQueryParts = ImmutableList
                .of("[\"amenity\"~\"bbq|biergarten|fast_food|ice_cream\"]", "[\"tourism\"=\"picnic_site\"]");
        final List<String> expectedDrivethroughEatingConstraintQueryParts = ImmutableList
                .of("[\"takeaway\"=\"yes\"]");
        final List<String> expectedIndoorSportConstraintQueryParts = ImmutableList
                .of("[\"sport\"~\"9pin|10pin|billiards|chess|darts|horse_racing|ice_hockey|ice_skating|sauna|shooting|swimming|table_tennis|table_soccer|tennis\"]", "[\"amenity\"=\"gym\"]");
        final List<String> expectedOutdoorSportConstraintQueryParts = ImmutableList
                .of("[\"sport\"~\"american_football|badminton|baseball|basketball|beachvolleyball|canoe|cliff_diving|climbing|climbing_adventure|equestrian|fishing|free_flying|golf|karting|kitesurfing|motocross|motor|orienteering|paragliding|sailing|scuba_diving|skiing|soccer|surfing|swimming|tennis\"]");
        final List<String> expectedIndoorEntertainmentConstraintQueryParts = ImmutableList
                .of("[\"amenity\"~\"brothel|casino|community_centre|gambling|nightclub|planetarium|social_centre|stripclub|swingerclub|stadium\"]", "[\"leisure\"~\"adult_gaming_centre|amusement_arcade|dance|hackerspace|water_park\"]");
        final List<String> expectedOutdoorEntertainmentConstraintQueryParts = ImmutableList
                .of("[\"leisure\"~\"beach_resort|bird_hide|firepit|garden|miniature_golf|park|playground|stadium|wildlife_hide\"]", "[\"tourism\"~\"theme_park|viewpoint|zoo\"]");
        final List<String> expectedTheatreCinemaConstraintQueryParts = ImmutableList
                .of("[\"amenity\"~\"cinema|community_centre|planetarium|theatre\"]");
        final List<String> expectedMuseumConstraintQueryParts = ImmutableList
                .of("[\"amenity\"=\"crypt\"]", "[\"building\"~\"bunker|cathedral|chapel|church|mosque|temple|shrine\"]",
                        "[\"historic\"]", "[\"military\"~\"airfield|bunker|barracks|naval_base\"]", "[\"tourism\"~\"museum|viewpoint\"]");
        final List<String> expectedMonumentConstraintQueryParts = ImmutableList
                .of("[\"amenity\"=\"crypt\"]", "[\"building\"~\"cathedral|chapel|church|mosque|temple|shrine\"]", "[\"historic\"]", "[\"military\"=\"naval_base\"]");
        final List<String> expectedShoppingCenterConstraintQueryParts = ImmutableList
                .of("[\"amenity\"=\"market_place\"]", "[\"shop\"~\"department_store|general|mall|supermarket\"]");

        //when
        final List<Constraint> indoorEatingConstraints = PoiTypeToConstraintMap.getConstraint(INDOOR_EATING).getConstraints();
        final List<Constraint> outdoorEatingConstraints = PoiTypeToConstraintMap.getConstraint(OUTDOOR_EATING).getConstraints();
        final List<Constraint> drivethroughEatingConstraints = PoiTypeToConstraintMap.getConstraint(DRIVETHROUGH_EATING).getConstraints();
        final List<Constraint> indoorSportConstraints = PoiTypeToConstraintMap.getConstraint(INDOOR_SPORTS).getConstraints();
        final List<Constraint> outdoorSportConstraints = PoiTypeToConstraintMap.getConstraint(OUTDOOR_SPORTS).getConstraints();
        final List<Constraint> indoorEntertainmentConstraints = PoiTypeToConstraintMap.getConstraint(INDOOR_ENTERTAINMENT).getConstraints();
        final List<Constraint> outdoorEntertainmentConstraints = PoiTypeToConstraintMap.getConstraint(OUTDOOR_ENTERTAINMENT).getConstraints();
        final List<Constraint> theatreCinemaConstraints = PoiTypeToConstraintMap.getConstraint(THEATRE_CINEMA).getConstraints();
        final List<Constraint> museumConstraints = PoiTypeToConstraintMap.getConstraint(MUSEUM).getConstraints();
        final List<Constraint> monumentConstraints = PoiTypeToConstraintMap.getConstraint(MONUMENTS).getConstraints();
        final List<Constraint> shoppingCenterConstraints = PoiTypeToConstraintMap.getConstraint(SHOPPING_CENTER).getConstraints();

        //then
        assertCorrectConstraintsQueryParts(expectedIndoorEatingConstraintQueryParts, indoorEatingConstraints);
        assertCorrectConstraintsQueryParts(expectedOutdoorEatingConstraintQueryParts, outdoorEatingConstraints);
        assertCorrectConstraintsQueryParts(expectedDrivethroughEatingConstraintQueryParts, drivethroughEatingConstraints);
        assertCorrectConstraintsQueryParts(expectedIndoorSportConstraintQueryParts, indoorSportConstraints);
        assertCorrectConstraintsQueryParts(expectedOutdoorSportConstraintQueryParts, outdoorSportConstraints);
        assertCorrectConstraintsQueryParts(expectedIndoorEntertainmentConstraintQueryParts, indoorEntertainmentConstraints);
        assertCorrectConstraintsQueryParts(expectedOutdoorEntertainmentConstraintQueryParts, outdoorEntertainmentConstraints);
        assertCorrectConstraintsQueryParts(expectedTheatreCinemaConstraintQueryParts, theatreCinemaConstraints);
        assertCorrectConstraintsQueryParts(expectedMuseumConstraintQueryParts, museumConstraints);
        assertCorrectConstraintsQueryParts(expectedMonumentConstraintQueryParts, monumentConstraints);
        assertCorrectConstraintsQueryParts(expectedShoppingCenterConstraintQueryParts, shoppingCenterConstraints);
    }

    private void assertCorrectConstraintsQueryParts(List<String> expectedConstraintQueryParts, List<Constraint> constraints) {
        assertEquals(expectedConstraintQueryParts.size(), constraints.size());
        for(int i=0; i<constraints.size(); i++) {
            assertEquals(expectedConstraintQueryParts.get(i), constraints.get(i).createQueryPart());
        }
    }

    @Test
    public void testOsmRequest() throws Exception {
        //given
        CompositeConstraint compositeConstraint = PoiTypeToConstraintMap.getConstraint(SHOPPING_CENTER);
        Location location = generateLocation(-11.120543, 143.117901);


        //when
        final OsmXmlRequest osmRequest = new OsmXmlRequest(compositeConstraint, location);

        //then
        assertEquals(
                "(node(around:5000.00,-11.120543,143.117901)[\"amenity\"=\"market_place\"];node(around:5000.00,-11.120543,143.117901)[\"shop\"~\"department_store|general|mall|supermarket\"]);out;",
                osmRequest.toString());
    }

    private Location generateLocation(double latitude, double longitude) {
        final Location location = new Location(OsmPoi.OSM_LOCATION_PROVIDER);
        location.setTime(0);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
