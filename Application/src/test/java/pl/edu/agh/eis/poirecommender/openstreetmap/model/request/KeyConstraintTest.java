package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyConstraintTest {
    private Constraint keyConstraint = new KeyConstraint("keyConstraint");

    @Test
    public void testNodeKeyConstraintRequestPartCreation() {
        //when
        String createdQueryPart = keyConstraint.createQueryPart();

        //then
        assertEquals("[\"keyConstraint\"]", createdQueryPart);
    }
}