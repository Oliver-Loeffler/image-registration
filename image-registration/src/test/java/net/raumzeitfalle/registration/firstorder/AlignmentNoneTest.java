/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.firstorder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.alignment.*;
import net.raumzeitfalle.registration.displacement.Displacement;

class AlignmentNoneTest {
	
	private Alignments classUnderTest = Alignments.UNALIGNED;
	
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
		RigidTransform residual = new RigidTransformCalculation().apply(result.getDisplacements(), setup.getAlignmentSelection());
		
		assertAll(
				()->assertEquals(-10.0, residual.getTranslationX() , 1E-11, "residual x-translation"),
				()->assertEquals( 15.0, residual.getTranslationY() , 1E-11, "residual y-translation"),
				()->assertEquals(  0.0, residual.getRotation() , 1E-11, "residual rotation"),
				()->assertEquals(result.getAlignment(), residual, "Calculated alignment shall match the alignment residual")
				);
	}
	
	@Test
	void alignmentSelection() {
			
		List<Displacement> alignmentSelection = displacements.stream()
														     .filter(setup.getAlignmentSelection())
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
