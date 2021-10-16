package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class ApacheMathCommonsSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		SolverProvider instance = SolverProvider.getInstance();
		
		SolverAdapter solver = instance.getSolver();
		assertEquals(ApacheMathCommonsSolver.class.getName(), solver.getClass().getName());
		
	}

}
