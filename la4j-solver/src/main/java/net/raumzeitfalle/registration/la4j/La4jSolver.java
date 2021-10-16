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

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.Matrix;
import org.la4j.decomposition.QRDecompositor;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class La4jSolver implements SolverAdapter {

	public Solution solve(References references, Deltas deviations) {

		Matrix laRef = Matrix.from2DArray(references.getArray());
		Matrix laDeltas = Matrix.from1DArray(deviations.rows(), 1, deviations.getArray());

		QRDecompositor laQr = new QRDecompositor(laRef);
		Matrix[] laQrResult = laQr.decompose();

		Matrix q = laQrResult[0];
		Matrix qTrans = q.transpose();

		Matrix r = laQrResult[1];
		Matrix rInv = InverterFactory.GAUSS_JORDAN.create(r).inverse();

		Matrix laRinvQTransp = rInv.multiply(qTrans);

		Matrix solved = laRinvQTransp.multiply(laDeltas);

		int rows = solved.rows();
		double[] coefficients = new double[solved.rows()];
		for (int i = 0; i < rows; i++) {
			coefficients[i] = solved.get(i, 0);
		}

		return Solutions.fromArray(coefficients);

	}

	@Override
	public Solution apply(References t, Deltas u) {
		return solve(t, u);
	}

}
