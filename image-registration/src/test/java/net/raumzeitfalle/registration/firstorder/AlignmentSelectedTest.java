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
import net.raumzeitfalle.registration.displacement.*;

class AlignmentSelectedTest {

private Alignments classUnderTest = Alignments.SELECTED;
	
	private final List<Displacement> displacements = createPointGridWith2AlignmentPoints();
	
	private final FirstOrderSetup setup = FirstOrderSetup.usingAlignment(classUnderTest)
													     .selectForAlignment(d->d.belongsTo(Category.ALIGN))
													     .build();

	@Test
	void alignmentCalculation() {
		
		FirstOrderResult result = new FirstOrderCorrection().apply(displacements, setup);
		
		assertAll(
				()->assertEquals( -4.0, result.getAlignment().getTranslationX(), 1E-11, "x-translation"),
				()->assertEquals(  5.0, result.getAlignment().getTranslationY(), 1E-11, "y-translation"),
				()->assertEquals(  0.0, result.getAlignment().getRotation(),     1E-11, "rotation"));
	}
	
	@Test
	void alignmentResidual() {
		
		FirstOrderResult result = new FirstOrderCorrection().apply(displacements, setup);
		RigidTransform residual = new RigidTransformCalculation()
				.apply(result.getDisplacements(), setup.getAlignmentSelection());
		
		assertAll(
				()->assertEquals(  0.0, residual.getTranslationX() , 1E-11, 
						"residual x-translation"),
				
				()->assertEquals(  0.0, residual.getTranslationY() , 1E-11, 
						"residual y-translation"),
				
				()->assertEquals(  0.0, residual.getRotation() , 1E-11, 
						"residual rotation"),
				
				()->assertNotEquals(result.getAlignment(), residual, 
						"alignment residual and actual alignment shall be different")
				);
	}
	
	@Test
	void alignmentSelection() {
			
		List<Displacement> alignmentSelection = displacements.stream()
														     .filter(setup.getAlignmentSelection())
														     .collect(Collectors.toList());
		
		assertAll(
				()->assertEquals(2, alignmentSelection.size(), "only 2 sites selected for alignment"),
				()->assertEquals(displacements.subList(25, 27), alignmentSelection)
				);
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
	
	private List<Displacement> createPointGridWith2AlignmentPoints() {
		List<Displacement> displacements = PointGrid.withNodes(5).moveBy(10, -15);
		
		int index = displacements.size();
		Displacement left  = Displacement.at(index+1, index+1, -72000, 0.0, -72000 - 4, 5.0, Category.ALIGN);
		Displacement right = Displacement.at(index+2, index+2,  72000, 0.0,  72000 - 4, 5.0, Category.ALIGN);
		
		displacements.add(left);
		displacements.add(right);
		return displacements;
	}
}
