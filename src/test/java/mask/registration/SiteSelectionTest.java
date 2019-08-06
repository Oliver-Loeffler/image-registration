package mask.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SiteSelectionTest {
	
	@Test
	void multipoint() {
		
		SiteSelection selection = SiteSelection.multipoint();
		List<Displacement> displacments = createDisplacementsList();
		
		long forAlignment = displacments.stream().filter(selection.getAlignment()).count();
		assertEquals(3, forAlignment);
		
		long forCalculation = displacments.stream().filter(selection.getCalculation()).count();
		assertEquals(3, forCalculation);
		
		long forFirstOrderCalc = displacments.stream().filter(selection.getFirstOrderSelection()).count();
		assertEquals(3, forFirstOrderCalc);
		
	}

	private List<Displacement> createDisplacementsList() {
		Displacement alignMark = Displacement.of(1,0, 0, 10, 10, SiteClass.ALIGN);
		Displacement regMark = Displacement.of(2,0, 0, 10, 10, SiteClass.REG_MARK);
		Displacement infoOnlyMark = Displacement.of(3,0, 0, 10, 10, SiteClass.INFO_ONLY);
		
		return Arrays.asList(alignMark,regMark, infoOnlyMark);
	}
}
