package net.raumzeitfalle.registration.solvertest;

import org.junit.jupiter.api.BeforeAll;

import net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;

class ApacheMathCommonsNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(ApacheMathCommonsSolver.class.getName());
	}

}
