/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration.displacement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
		
	}
	
	@Test
	void alignOn() {
		
		AlignmentSelection alignSelection = SiteSelection.alignOn(Category.INFO_ONLY);
		SiteSelection siteSelection = alignSelection.build();
		
		assertEquals(alignSelection.get(), siteSelection.getAlignment());
		
		List<Displacement> displacments = createDisplacementsList();
		
		long forAlignment = displacments.stream().filter(siteSelection.getAlignment()).count();
		assertEquals(1, forAlignment);
		
		long forCalculation = displacments.stream().filter(siteSelection.getCalculation()).count();
		assertEquals(1, forCalculation);
		
		
	}
	
	@Test
	void remove() {
		
		Predicate<Displacement> toBeRemoved = d->d.belongsTo(Category.ALIGN)
				   || d.belongsTo(Category.INFO_ONLY);
		
		SiteSelection siteSelection = SiteSelection.multipoint().remove(toBeRemoved);
		
		List<Displacement> displacments = createDisplacementsListWithAlign();
		
		long forAlignment = displacments.stream().filter(siteSelection.getAlignment()).count();
		assertEquals(3, forAlignment);
		
		long forRemoval = displacments.stream().filter(siteSelection.getSitesToRemove()).count();
		assertEquals(2, forRemoval);
		
		long forCalculation = displacments.stream().filter(siteSelection.getCalculation()).count();
		assertEquals(1, forCalculation);
	}
	
	@Test
	void removeByCategory() {
		
		SiteSelection siteSelection = SiteSelection.alignOn(Category.REG)
											       .forCalculation(d->true)
											       .remove(Category.INFO_ONLY);
		
		List<Displacement> displacments = createDisplacementsList();
		
		long forAlignment = displacments.stream().filter(siteSelection.getAlignment()).count();
		assertEquals(2, forAlignment);
		
		long forRemoval = displacments.stream().filter(siteSelection.getSitesToRemove()).count();
		assertEquals(1, forRemoval);
		
		long forCalculation = displacments.stream().filter(siteSelection.getCalculation()).count();
		assertEquals(2, forCalculation);
	}
	
	@Test
	void removeByPredicate() {
		
		Predicate<Displacement> toBeRemoved = d->d.belongsTo(Category.ALIGN)
				   || d.belongsTo(Category.INFO_ONLY);
		
		SiteSelection siteSelection = SiteSelection.forAlignment(d->true)
											       .forCalculation(d->true)
											       .remove(toBeRemoved);
		
		List<Displacement> displacments = createDisplacementsListWithAlign();
		
		long forAlignment = displacments.stream().filter(siteSelection.getAlignment()).count();
		assertEquals(3, forAlignment);
		
		long forRemoval = displacments.stream().filter(siteSelection.getSitesToRemove()).count();
		assertEquals(2, forRemoval);
		
		long forCalculation = displacments.stream().filter(siteSelection.getCalculation()).count();
		assertEquals(1, forCalculation);
	}

	private List<Displacement> createDisplacementsList() {
		Displacement alignMark = Displacement.at(1,1,0, 0, 10, 10, Category.REG);
		Displacement regMark = Displacement.at(2,2,0, 0, 10, 10, Category.REG);
		Displacement infoOnlyMark = Displacement.at(3,3,0, 0, 10, 10, Category.INFO_ONLY);
		
		return Arrays.asList(alignMark,regMark, infoOnlyMark);
	}
	
	private List<Displacement> createDisplacementsListWithAlign() {
		Displacement alignMark = Displacement.at(1,1,0, 0, 10, 10, Category.ALIGN);
		Displacement regMark = Displacement.at(2,2,0, 0, 10, 10, Category.REG);
		Displacement infoOnlyMark = Displacement.at(3,3,0, 0, 10, 10, Category.INFO_ONLY);
		
		return Arrays.asList(alignMark,regMark, infoOnlyMark);
	}
}
