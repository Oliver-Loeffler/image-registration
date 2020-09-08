package net.raumzeitfalle.registration.firstorder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.alignment.*;
import net.raumzeitfalle.registration.displacement.Displacement;

class AlignmentAllTest {
	
	private Alignments classUnderTest = Alignments.ALL;
	
	private final List<Displacement> displacements = PointGrid.withNodes(5).moveBy(10, -15);
	
	private final FirstOrderSetup setup = FirstOrderSetup.usingAlignment(classUnderTest).build();

	@Test
	void alignmentCalculation() {
		
		FirstOrderResult result = new FirstOrderCorrection().apply(displacements, setup);
		
		assertAll(
				()->assertEquals(-10.0, result.getAlignment().getTranslationX(), 1E-11, "x-translation"),
				()->assertEquals( 15.0, result.getAlignment().getTranslationY(), 1E-11, "y-translation"),
				()->assertEquals(  0.0, result.getAlignment().getRotation(),     1E-11, "rotation"));
	}
	
	@Test
	void alignmentResidual() {
		
		FirstOrderResult result = new FirstOrderCorrection().apply(displacements, setup);
		RigidTransform residual = new RigidTransformCalculation().apply(result.getDisplacements(), setup.getAlignmenSelection());
		
		assertAll(
				()->assertEquals(  0.0, residual.getTranslationX() , 1E-11, 
						"residual x-translation"),
				
				()->assertEquals(  0.0, residual.getTranslationY() , 1E-11, 
						"residual y-translation"),
				
				()->assertEquals(  0.0, residual.getRotation() , 1E-11, 
						"residual rotation"),
				
				()->assertNotEquals(result.getAlignment(), residual, 
						"Calculated alignment must NOT match the alignment residual")
				);
	}
	
	@Test
	void alignmentSelection() {
			
		List<Displacement> alignmentSelection = displacements.stream()
														     .filter(setup.getAlignmenSelection())
														     .collect(Collectors.toList());
		
		assertEquals(displacements, alignmentSelection);
		
	}
	
	@Test
	void calculationSelection() {

		List<Displacement> calculationSelection = displacements.stream()
														       .filter(setup.getCalculationSelection())
														       .collect(Collectors.toList());
		
		assertEquals(displacements, calculationSelection);
		
	}
	
	@Test
	void removalSelection() {

		List<Displacement> seletedForRemoval = displacements.stream()
													        .filter(setup.getRemovalSelection())
													        .collect(Collectors.toList());
		
		assertTrue(seletedForRemoval.isEmpty());
		
	}
	

}
