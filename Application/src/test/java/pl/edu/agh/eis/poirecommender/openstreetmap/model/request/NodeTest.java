package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class NodeTest {
    @Mock
    private Area areaMock;
    @Mock
    private Constraint constraintMock;

    @InjectMocks
    private Node node;

    @Test
    public void testNodeRequestPartCreation() {
        //given
        given(areaMock.createQueryPart()).willReturn("_AREA_");
        given(constraintMock.createQueryPart()).willReturn("+CONSTRAINT+");

        //when
        String createdQuery = node.createQuery();

        //then
        assertEquals("node_AREA_+CONSTRAINT+", createdQuery);
    }
}