package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.location.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AroundAreaTest {
    @Mock
    private Location locationMock;

    @Test
    public void shouldCreateCorrectArountAreaRequestPart() {
        //given
        given(locationMock.getLatitude()).willReturn(10.15161);
        given(locationMock.getLongitude()).willReturn(20.16171801);
        Area aroundArea = new AroundArea(locationMock, 300.1701f);

        //when
        String createdQueryPart = aroundArea.createQueryPart();

        //then
        assertEquals("(around:300.17,10.151610,20.161718)", createdQueryPart);
    }
}