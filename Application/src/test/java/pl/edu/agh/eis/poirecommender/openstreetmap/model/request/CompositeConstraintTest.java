package pl.edu.agh.eis.poirecommender.openstreetmap.model.request;

import com.google.common.collect.FluentIterable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CompositeConstraintTest {
    @Mock
    private Constraint constraint1Row1Mock;
    @Mock
    private Constraint constraint2Row1Mock;
    @Mock
    private Constraint constraint3Row1Mock;
    @Mock
    private Constraint constraint1Row2Mock;
    @Mock
    private Constraint constraint2Row2Mock;
    @Mock
    private Constraint constraint1Row3Mock;

    @Test
    public void shouldCreateCorrectCompositeConstraint() {
        //given
        given(constraint1Row1Mock.createQueryPart()).willReturn(".CONSTRAINT_1_1.");
        given(constraint1Row2Mock.createQueryPart()).willReturn("+CONSTRAINT_1_2+");
        given(constraint1Row3Mock.createQueryPart()).willReturn("-CONSTRAINT_1_3-");
        given(constraint2Row1Mock.createQueryPart()).willReturn("_CONSTRAINT_2_1_");
        given(constraint2Row2Mock.createQueryPart()).willReturn("*CONSTRAINT_2_2*");
        given(constraint3Row1Mock.createQueryPart()).willReturn("#CONSTRAINT_3#");
        CompositeConstraint compositeConstraint = new CompositeConstraint
                .Builder(constraint1Row1Mock).and(constraint1Row2Mock).and(constraint1Row3Mock)
                .or(constraint2Row1Mock).and(constraint2Row2Mock)
                .or(constraint3Row1Mock)
                .build();

        //when
        List<String> compositeQueries = FluentIterable.from(compositeConstraint.getConstraints())
                .transform(QueryPart.TO_STRING).toList();

        //then
        assertEquals(compositeQueries, Arrays.asList(
                ".CONSTRAINT_1_1.+CONSTRAINT_1_2+-CONSTRAINT_1_3-",
                "_CONSTRAINT_2_1_*CONSTRAINT_2_2*",
                "#CONSTRAINT_3#"));
    }
}