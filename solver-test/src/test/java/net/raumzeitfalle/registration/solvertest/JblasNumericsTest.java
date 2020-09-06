package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import net.raumzeitfalle.registration.jblas.JblasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;


class JblasSolverNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JblasSolver.class.getName());
	}
	
	@BeforeEach
	void checkImplementation() {
		
		Solver jblasSolver = new JblasSolver();
		
		assertEquals(jblasSolver.getClass(), SolverProvider.getInstance().getSolver().getClass());
	}

}
