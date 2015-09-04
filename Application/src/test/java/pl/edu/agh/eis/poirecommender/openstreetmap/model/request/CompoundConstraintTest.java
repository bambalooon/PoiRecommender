package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CompoundConstraintTest {
    @Mock
    private Constraint constraintMock1;
    @Mock
    private Constraint constraintMock2;
    @Mock
    private Constraint constraintMock3;

    @Test
    public void shouldCreateCorrectCompoundConstraint() {
        //given
        given(constraintMock1.createQueryPart()).willReturn(":CONSTRAINT_1:");
        given(constraintMock2.createQueryPart()).willReturn("-CONSTRAINT_2-");
        given(constraintMock3.createQueryPart()).willReturn("+CONSTRAINT_3+");
        Constraint compoundConstraint = new CompoundConstraint(
                Arrays.asList(constraintMock1, constraintMock2, constraintMock3));

        //when
        String createdQueryPart = compoundConstraint.createQueryPart();

        //then
        assertEquals(":CONSTRAINT_1:-CONSTRAINT_2-+CONSTRAINT_3+", createdQueryPart);
    }
}