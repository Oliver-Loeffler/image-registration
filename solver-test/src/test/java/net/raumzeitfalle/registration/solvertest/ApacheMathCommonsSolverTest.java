package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.jama.JamaSolver;
import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.*;

class ApacheMathCommonsSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		SolverProvider instance = SolverProvider.getInstance();
		
		Solver solver = instance.getSolver();
		assertEquals(ApacheMathCommonsSolver.class.getName(), solver.getClass().getName());
		
		Solver otherSolver = instance.getSolver();
		assertEquals(ApacheMathCommonsSolver.class.getName(), otherSolver.getClass().getName());
		
		assertEquals(solver, otherSolver, "due to caching, both should be the same instance");
		
		// Now force SolverProvide to go a different way
		SolverProvider.setPreferredImplementation(JamaSolver.class.getName());
		
		Solver jamaSolver = instance.getSolver();
		assertEquals(JamaSolver.class.getName(), jamaSolver.getClass().getName());
		
		// Now force SolverProvide to use SPI
		SolverProvider.setPreferredImplementation(null);
		
		Solver bySpi = instance.getSolver();
		assertNotNull(bySpi);
	}

}
