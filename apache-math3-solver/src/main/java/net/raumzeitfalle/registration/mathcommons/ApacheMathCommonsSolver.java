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
package net.raumzeitfalle.registration.mathcommons;

import org.apache.commons.math3.linear.*;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class ApacheMathCommonsSolver implements SolverAdapter {

	public Solution solve(References references, Deltas deviations) {

		RealMatrix refs = MatrixUtils.createRealMatrix(references.getArray());
		RealMatrix deltas = MatrixUtils.createColumnRealMatrix(deviations.getArray());

		QRDecomposition qr = new QRDecomposition(refs);

		RealMatrix rInverse = createInverse(qr);

		RealMatrix qTransposed = qr.getQ().transpose();

		RealMatrix solution = rInverse.multiply(qTransposed).multiply(deltas);

		return Solutions.fromArray(solution.getColumn(0));

	}

	private RealMatrix createInverse(QRDecomposition qr) {

		RealMatrix r = qr.getR();

		if (r.isSquare()) {
			return MatrixUtils.inverse(r);
		}

		return new SingularValueDecomposition(r).getSolver().getInverse();
	}

}
