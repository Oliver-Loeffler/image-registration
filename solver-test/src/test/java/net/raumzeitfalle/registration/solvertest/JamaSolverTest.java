package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.jama.JamaSolver;
import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.*;


class JamaSolverTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JamaSolver.class.getName());
	}

	@Test
	void testServiceDiscovery() {
		
		Solver solver = SolverProvider.getInstance().getSolver();
		
		assertEquals(JamaSolver.class.getName(), solver.getClass().getName());
		
	}

}
