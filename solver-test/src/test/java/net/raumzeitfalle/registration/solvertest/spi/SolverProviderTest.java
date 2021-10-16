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
package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class SolverProviderTest {
	
	private SolverProvider classUnderTest;

	@Test
	void unknownSolver_getInstance() {
		
		SolverProvider.setPreferredImplementation("thisClassDoesNotExist");
		
		classUnderTest = SolverProvider.getInstance();
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getSolver());	
		
		assertEquals("There is no solver with class [thisClassDoesNotExist] configured.", t.getMessage());
		
	}
	
	@Test
	void anySolverRequested() {
		
		SolverProvider.setPreferredImplementation(null);
		
		classUnderTest = SolverProvider.getInstance();
		
		assertDoesNotThrow(()->{
			
			SolverAdapter solver = classUnderTest.getSolver();
			
			assertNotNull(solver);
			
			
		});
		
	}
	
	@Test
	void cachingAndRediscovery() {

		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
		SolverProvider classUnderTest = SolverProvider.getInstance();
		SolverAdapter solver = classUnderTest.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), solver.getClass().getName());

		// use cache
		SolverAdapter otherSolver = classUnderTest.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), otherSolver.getClass().getName());
		assertEquals(solver, otherSolver, "due to caching, both should be the same instance");

		// Now force SolverProvider to prefer different Solver class
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());

		SolverAdapter ejmlSolver = classUnderTest.getSolver();
		assertEquals(EjmlSolver.class.getName(), ejmlSolver.getClass().getName());

		// Now require SolverProvider to perform full discovery of implementations (no caching, no preference)
		SolverProvider.setPreferredImplementation(null);

		SolverAdapter bySpi = classUnderTest.getSolver();
		assertNotNull(bySpi);

	}

}
