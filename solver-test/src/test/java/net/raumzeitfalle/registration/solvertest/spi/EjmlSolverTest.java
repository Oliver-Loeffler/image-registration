package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class EjmlSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(EjmlSolver.class.getName(), solver.getClass().getName());
		
	}

}
