package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class KeyMultipleValueConstraintTest {
    private Constraint keyMultipleValueConstraint = new KeyMultipleValueConstraint(
            "constraintKey", ImmutableList.of("constraintValue1", "constraintValue2", "constraintValue3"));

    @Test
    public void shouldCreateCorrectKeyMultipleValueConstraintRequestPart() {
        //when
        String createdQueryPart = keyMultipleValueConstraint.createQueryPart();

        //then
        assertEquals("[\"constraintKey\"~\"constraintValue1|constraintValue2|constraintValue3\"]", createdQueryPart);
    }
}