package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import net.raumzeitfalle.registration.jblas.JBlasSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;


class JBlasSolverNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(JBlasSolver.class.getName());
	}
	
	@BeforeEach
	void checkImplementation() {
		
		Solver jblasSolver = new JBlasSolver();
		
		assertEquals(jblasSolver.getClass(), SolverProvider.getInstance().getSolver().getClass());
	}

}
