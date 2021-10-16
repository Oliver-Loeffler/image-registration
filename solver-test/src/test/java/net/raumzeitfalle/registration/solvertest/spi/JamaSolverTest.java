package net.raumzeitfalle.registration.solvertest.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.jama.JamaSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;


class JamaSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JamaSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		assertEquals(JamaSolver.class.getName(), solver.getClass().getName());		
	}
		

}
