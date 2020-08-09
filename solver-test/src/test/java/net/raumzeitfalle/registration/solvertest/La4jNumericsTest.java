package net.raumzeitfalle.registration.solvertest;

import org.junit.jupiter.api.BeforeAll;

import net.raumzeitfalle.registration.la4j.La4jSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;


class La4jSolverNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(La4jSolver.class.getName());
	}

}
