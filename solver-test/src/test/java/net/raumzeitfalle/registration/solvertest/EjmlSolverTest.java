package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.*;

class EjmlSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		Solver solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(EjmlSolver.class.getName(), solver.getClass().getName());
		
	}

}
