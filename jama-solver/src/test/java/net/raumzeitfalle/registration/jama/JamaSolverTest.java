package net.raumzeitfalle.registration.jama;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.spi.Solver;

class JamaSolverTest {

	private final Solver classUnderTest = new JamaSolver();

	private static final double TOLERANCE = 1E-11;
	
	@Test
	void test() {

		double[][] design = { 
				{ -75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ -75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, -75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, -75000.0, 0.0, 0.0, 1.0 } };

		double[] differences = { -0.075, 0.140, -0.075, -0.140, 0.075, -0.140, 0.075, 0.140 };

		
		Solution solution = classUnderTest.apply(() -> design, () -> differences);

		double[] result = { 1.0E-6, -2.0E-6, 0.0, 0.0, 0.0, 0.0 };

		assertAll(() -> assertNotNull(solution, "must not be null"),

				() -> assertEquals(result[0], solution.get(0), TOLERANCE, "scale x"),
				() -> assertEquals(result[1], solution.get(1), TOLERANCE, "scale y"),

				() -> assertEquals(result[2], solution.get(2), TOLERANCE, "ortho x"),
				() -> assertEquals(result[3], solution.get(3), TOLERANCE, "ortho y"),

				() -> assertEquals(result[4], solution.get(4), TOLERANCE, "trans x"),
				() -> assertEquals(result[5], solution.get(5), TOLERANCE, "trans y"));

	}

}
