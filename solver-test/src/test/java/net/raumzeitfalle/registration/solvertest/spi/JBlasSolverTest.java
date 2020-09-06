package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.jblas.JBlasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;


class JBlasSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JBlasSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		Solver solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(JBlasSolver.class.getName(), solver.getClass().getName());
		
	}

}
