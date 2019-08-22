/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.displacement;

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
		Displacement alignMark = Displacement.at(1,1,0, 0, 10, 10, DisplacementClass.ALIGN);
		Displacement regMark = Displacement.at(2,2,0, 0, 10, 10, DisplacementClass.REG);
		Displacement infoOnlyMark = Displacement.at(3,3,0, 0, 10, 10, DisplacementClass.INFO_ONLY);
		
		return Arrays.asList(alignMark,regMark, infoOnlyMark);
	}
}
