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
package net.raumzeitfalle.jama;

import net.raumzeitfalle.registration.solver.Deltas;
import net.raumzeitfalle.registration.solver.References;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.Solutions;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class Solver implements SolverAdapter {
	
	public Solution solve(References references, Deltas deviations) {
		
		Matrix refs = new Matrix(references.getArray());
		Matrix deltas = new Matrix(deviations.getArray(), deviations.rows());
		
		QRDecomposition qr = new QRDecomposition(refs);
		
		Matrix rInverse = qr.getR().inverse();
		Matrix qTransposed = qr.getQ().transpose();
		Matrix solution = rInverse.times(qTransposed)
								  .times(deltas);
		
		double[] firstColumn = getFirstColumn(solution);
		return Solutions.fromArray(firstColumn);
		
	}

	private double[] getFirstColumn(Matrix solution) {	
		double[] column = new double[solution.getRowDimension()];
		for (int row = 0; row < column.length; row++) {
			column[row] = solution.get(row, 0);
		}
		return column;
	}

	@Override
	public Solution apply(References t, Deltas u) {
		return solve(t, u);
	}
}
