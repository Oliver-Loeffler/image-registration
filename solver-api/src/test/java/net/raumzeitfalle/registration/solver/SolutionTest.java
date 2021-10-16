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
package net.raumzeitfalle.registration.solver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void test() {
		
		double[] possibleSolution = {0,1,2,3,4,5};
		
		Solution solution = Solutions.fromArray(possibleSolution);
		
		assertAll(
				()->assertEquals(possibleSolution[0], solution.get(0)),
				()->assertEquals(possibleSolution[1], solution.get(1)),
				()->assertEquals(possibleSolution[2], solution.get(2)),
				()->assertEquals(possibleSolution[3], solution.get(3)),
				()->assertEquals(possibleSolution[4], solution.get(4)),
				()->assertEquals(possibleSolution[5], solution.get(5))
				);
	
	}
	
	@Test
	void illegalCoefficients_negativeCoefficientIndex() {
		
		Solution solution = Solutions.fromArray(new double[]{0,1,2});
		
		Throwable t = assertThrows(IllegalArgumentException.class, ()->solution.get(-1));
		
		assertEquals("index must be > or = to zero", t.getMessage());
		
	}
	
	@Test
	void illegalCoefficients_coefficientNotAvailable() {
		
		Solution solution = Solutions.fromArray(new double[]{0,1,2});
		
		Throwable t = assertThrows(IllegalArgumentException.class, ()->solution.get(17));
		
		assertEquals("index must be in intervall [0, 2]", t.getMessage());
		
	}
	
	@Test
	void illegalSolutions_null() {
		
		Throwable t = assertThrows(NullPointerException.class, ()->Solutions.fromArray(null));
		
		assertEquals("array of solution coefficients must not be null", t.getMessage());
		
	}
	
	@Test
	void emptySolution() {
		
		Solution solution = Solutions.fromArray(new double[0]);
		
		Throwable t = assertThrows(IllegalArgumentException.class, ()->solution.get(17));
		
		assertEquals("The solution is empty, no coefficients available.", t.getMessage());
		
	}

}
