package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.la4j.La4jSolver;
import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.*;


class La4jSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(La4jSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		Solver solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(La4jSolver.class.getName(), solver.getClass().getName());
		
	}

}
