package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
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