/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.la4j;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class La4jSolverTest {

	private final SolverAdapter classUnderTest = new La4jSolver();

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
