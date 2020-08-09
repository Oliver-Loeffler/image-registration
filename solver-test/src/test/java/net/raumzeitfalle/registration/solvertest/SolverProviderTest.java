package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;

class SolverProviderTest {

	@Test
	void unknownSolverRequested() {
		
		SolverProvider.setPreferredImplementation("thisClassDoesNotExist");
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->SolverProvider.getInstance().getSolver());	
		
		assertEquals("There is no solver with class [thisClassDoesNotExist] configured.", t.getMessage());
		
	}
	
	@Test
	void anySolverRequested() {
		
		SolverProvider.setPreferredImplementation(null);
		
		assertDoesNotThrow(()->{
			
			Solver solver = SolverProvider.getInstance().getSolver();
			
			assertNotNull(solver);
			
			
		});
		
	}
	
	@Test
	void cachingAndRediscovery() {

		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
		SolverProvider instance = SolverProvider.getInstance();
		Solver solver = instance.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), solver.getClass().getName());

		// use cache
		Solver otherSolver = instance.getSolver();
		
		assertEquals(ApacheMathCommonsSolver.class.getName(), otherSolver.getClass().getName());
		assertEquals(solver, otherSolver, "due to caching, both should be the same instance");

		// Now force SolverProvider to prefer different Solver class
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());

		Solver ejmlSolver = instance.getSolver();
		assertEquals(EjmlSolver.class.getName(), ejmlSolver.getClass().getName());

		// Now require SolverProvider to perform full discovery of implementations (no caching, no preference)
		SolverProvider.setPreferredImplementation(null);

		Solver bySpi = instance.getSolver();
		assertNotNull(bySpi);

	}

}
