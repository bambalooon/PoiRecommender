package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class KeyValueEqualConstraintTest {
    private Constraint keyValueConstraint = new KeyValueEqualConstraint("constraintKey", "constraintValue");

    @Test
    public void shouldCreateCorrectKeyValueConstraintRequestPart() {
        //when
        String createdQueryPart = keyValueConstraint.createQueryPart();

        //then
        assertEquals("[\"constraintKey\"=\"constraintValue\"]", createdQueryPart);
    }
}