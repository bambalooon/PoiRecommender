package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.edu.agh.eis.poirecommender.pois.model.OsmPoi;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AroundAreaTest {
    private static final Location LOCATION;
    static {
        LOCATION = new Location(OsmPoi.OSM_LOCATION_PROVIDER);
        LOCATION.setTime(0);
        LOCATION.setLatitude(10.15161);
        LOCATION.setLongitude(20.16171801);
    }

    private Area aroundArea = new AroundArea(LOCATION, 300.1701f);

    @Test
    public void shouldCreateCorrectArountAreaRequestPart() {
        //when
        String createdQueryPart = aroundArea.createQueryPart();

        //then
        assertEquals("(around:300.17,10.151610,20.161718)", createdQueryPart);
    }
}