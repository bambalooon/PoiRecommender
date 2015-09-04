package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.support.test.runner.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class KeyMultipleValueConstraintTest {
    private Constraint keyMultipleValueConstraint = new KeyMultipleValueConstraint(
            "constraintKey", ImmutableList.of("constraintValue1", "constraintValue2", "constraintValue3"));

    @Test
    public void testNodeKeyMultipleValueConstraintRequestPartCreation() {
        //when
        String createdQueryPart = keyMultipleValueConstraint.createQueryPart();

        //then
        assertEquals("[\"constraintKey\"~\"constraintValue1|constraintValue2|constraintValue3\"]", createdQueryPart);
    }
}