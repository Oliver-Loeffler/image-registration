package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.la4j.La4jSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;


class La4jSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(La4jSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(La4jSolver.class.getName(), solver.getClass().getName());
		
	}

}
