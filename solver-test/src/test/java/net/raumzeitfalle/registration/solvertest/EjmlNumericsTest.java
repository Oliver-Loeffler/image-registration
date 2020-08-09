package net.raumzeitfalle.registration.solvertest;

import org.junit.jupiter.api.BeforeAll;

import net.raumzeitfalle.registration.ejml.EjmlSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;

class EjmlNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(EjmlSolver.class.getName());
	}

}
