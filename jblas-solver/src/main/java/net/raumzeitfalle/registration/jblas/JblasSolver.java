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
package net.raumzeitfalle.registration.jblas;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;

import net.raumzeitfalle.registration.solver.Deltas;
import net.raumzeitfalle.registration.solver.References;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.Solutions;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class JblasSolver implements SolverAdapter {

	public Solution solve(References references, Deltas deviations) {
		
		DoubleMatrix refs = new DoubleMatrix(references.getArray());
		DoubleMatrix deltas = new DoubleMatrix(deviations.getArray());
		DoubleMatrix coeffs = Solve.solveLeastSquares(refs, deltas);
		
		int rows = coeffs.getLength();

		double[] coefficients = new double[rows];
		for (int i = 0; i < rows; i++) {
			coefficients[i] = coeffs.get(i, 0);
		}
		
		return Solutions.fromArray(coefficients);		
	}

}
