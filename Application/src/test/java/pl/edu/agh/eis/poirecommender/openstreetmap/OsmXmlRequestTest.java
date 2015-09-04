package pl.edu.agh.eis.poirecommender.openstreetmap;

import android.location.Location;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.CompositeConstraint;
import pl.edu.agh.eis.poirecommender.openstreetmap.model.request.Constraint;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OsmXmlRequestTest {
    @Mock
    private CompositeConstraint compositeConstraintMock;
    @Mock
    private Constraint constraintMock1;
    @Mock
    private Constraint constraintMock2;
    @Mock
    private Constraint constraintMock3;
    @Mock
    private Location locationMock;

    @Before
    public void setUp() {
        given(compositeConstraintMock.getConstraints())
                .willReturn(ImmutableList.of(constraintMock1, constraintMock2, constraintMock3));
        given(constraintMock1.createQueryPart()).willReturn("_CONSTRAINT_1_");

        given(locationMock.getLatitude()).willReturn(-11.120543);
        given(locationMock.getLongitude()).willReturn(143.117901);
    }

    @Test
    public void shouldCreateCorrectOsmXmlRequestFromCompositeConstraint() {
        //given
        given(constraintMock2.createQueryPart()).willReturn("|CONSTRAINT_2|");
        given(constraintMock3.createQueryPart()).willReturn("*CONSTRAINT_3*");

        //when
        final OsmXmlRequest osmRequest = new OsmXmlRequest(compositeConstraintMock, locationMock);

        //then
        assertEquals("(" +
                        "node(around:5000.00,-11.120543,143.117901)_CONSTRAINT_1_;" +
                        "node(around:5000.00,-11.120543,143.117901)|CONSTRAINT_2|;" +
                        "node(around:5000.00,-11.120543,143.117901)*CONSTRAINT_3*" +
                    ");out;", osmRequest.toString());
    }

    @Test
    public void shouldCreateCorrectOsmXmlRequestFromSingleConstraint() {
        //when
        final OsmXmlRequest osmRequest = new OsmXmlRequest(constraintMock1, locationMock);

        //then
        assertEquals("(node(around:5000.00,-11.120543,143.117901)_CONSTRAINT_1_);out;", osmRequest.toString());
    }
}