package net.raumzeitfalle.registration.solvertest;

import org.junit.jupiter.api.BeforeAll;

import net.raumzeitfalle.registration.jama.JamaSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;


class JamaNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JamaSolver.class.getName());
	}

}
