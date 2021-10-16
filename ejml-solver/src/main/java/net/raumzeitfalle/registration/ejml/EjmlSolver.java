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
package net.raumzeitfalle.registration.ejml;

import org.ejml.simple.SimpleMatrix;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class EjmlSolver implements SolverAdapter {

	public Solution solve(References references, Deltas deviations) {
		
		SimpleMatrix refs = new SimpleMatrix(references.getArray());
		SimpleMatrix deltas = new SimpleMatrix(deviations.rows(), 1, true, deviations.getArray());
		
		SimpleMatrix coeffs = refs.solve(deltas);
		
		int rows = coeffs.numRows();
		double[] coefficients = new double[coeffs.numRows()];
		
		for (int i = 0; i < rows; i++) {
			coefficients[i] = coeffs.get(i, 0);
		}
		
		return Solutions.fromArray(coefficients);
		
	}

}
