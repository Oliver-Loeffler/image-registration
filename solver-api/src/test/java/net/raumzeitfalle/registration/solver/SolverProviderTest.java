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

import java.util.List;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class SolverProviderTest {

	private SolverProvider classUnderTest = SolverProvider.getInstance();

	@BeforeEach
	void prepare() {
		SolverProvider.setPreferredImplementation(null);
	}
	
	@Test
	void getAllAvailableImplementations() {
		
		List<SolverAdapter> implementations = classUnderTest.getAllAvailableImplementations();
		
		assertTrue(implementations.isEmpty(), "no implementations expected to be found");
		
	}
	
	@Test
	void getSolver_missingConfiguration() {
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getSolver());
		
		assertEquals("There is no solver implementation configured.", t.getMessage());
		
	}
	
	@Test
	void useOfCustomSolver_missingCustomSolver() {
		SolverProvider.setPreferredImplementation("myCustomSolverClass");
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getSolver());
		
		assertEquals("There is no solver with class [myCustomSolverClass] configured.", t.getMessage());
	}

}
