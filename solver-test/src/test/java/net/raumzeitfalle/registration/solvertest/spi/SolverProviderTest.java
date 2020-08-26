package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;

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
			
			Solver solver = classUnderTest.getSolver();
			
			assertNotNull(solver);
			
			
		});
		
	}
	
	@Test
	void cachingAndRediscovery() {

		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
		SolverProvider classUnderTest = SolverProvider.getInstance();
		Solver solver = classUnderTest.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), solver.getClass().getName());

		// use cache
		Solver otherSolver = classUnderTest.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), otherSolver.getClass().getName());
		assertEquals(solver, otherSolver, "due to caching, both should be the same instance");

		// Now force SolverProvider to prefer different Solver class
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());

		Solver ejmlSolver = classUnderTest.getSolver();
		assertEquals(EjmlSolver.class.getName(), ejmlSolver.getClass().getName());

		// Now require SolverProvider to perform full discovery of implementations (no caching, no preference)
		SolverProvider.setPreferredImplementation(null);

		Solver bySpi = classUnderTest.getSolver();
		assertNotNull(bySpi);

	}

}
