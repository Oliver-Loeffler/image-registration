package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.la4j.La4jSolver;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;


class La4jSolverNumericsTest extends NumericsTest {
	
	@BeforeAll
	public static void prepare() {
		SolverProvider.setPreferredImplementation(La4jSolver.class.getName());
	}
	
	@BeforeEach
	void checkImplementation() {
		
		Solver la4j = new La4jSolver();
		
		assertEquals(la4j.getClass(), SolverProvider.getInstance().getSolver().getClass());
	}

}
