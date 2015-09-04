package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class KeyValueEqualConstraintTest {
    private Constraint keyValueConstraint = new KeyValueEqualConstraint("constraintKey", "constraintValue");

    @Test
    public void testNodeKeyValueConstraintRequestPartCreation() {
        //when
        String createdQueryPart = keyValueConstraint.createQueryPart();

        //then
        assertEquals("[\"constraintKey\"=\"constraintValue\"]", createdQueryPart);
    }
}