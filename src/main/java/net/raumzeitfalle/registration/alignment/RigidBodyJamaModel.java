/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;
import Jama.QRDecomposition;

/**
 * 
 * Calculates translation and rotation for the given set of equations.
 * A rigid transform preserves all distances between points.
 *
 * <ul>
 * 	<li>Translation</li>
 *  <li>Rotation</li>
 * </ul>
 *
 * @author oliver
 * 
 */
final class RigidBodyJamaModel implements RigidBodyModel {
	
	private void prepare(Collection<RigidModelEquation> equations, Matrix references, Matrix deltas) {
		int row = 0;
		Iterator<RigidModelEquation> it = equations.iterator();
		while(it.hasNext()) {
			row = addEquation(references, deltas, row, it.next());
		}
	}

	private int addEquation(Matrix references, Matrix deltas, int row, RigidModelEquation eq) {
		references.set(row, 0, eq.getXf());
		references.set(row, 1, eq.getYf());
		references.set(row, 2, eq.getDesignValue());
		deltas.set(row, 0, eq.getDeltaValue());
		row++;
		return row;
	}
	
	private SimpleRigidTransform solve(Matrix references, Matrix deltas) {
		
		QRDecomposition qr = new QRDecomposition(references);
		Matrix Rinverse = qr.getR().inverse();
		Matrix Qtransposed = qr.getQ().transpose();
		Matrix solved = Rinverse.times(Qtransposed).times(deltas);
	
		return SimpleRigidTransform
				.with(solved.get(0, 0), solved.get(1, 0), solved.get(2, 0));
	}

	@Override
	public RigidTransform solve(Collection<RigidModelEquation> equations) {
		/* 
		 * TODO: Depending on equations, the model should decide to include or exclude one dimension
		 * or instead to populate one axis fully with 0.0.
		 * 
		 * TODO: How to handle situations where the system of equations cannot be solved?
		 * 
		 */
		int rows = equations.size();
		Matrix references = new Matrix(rows, 3);
		Matrix deltas = new Matrix(rows, 1);
		
		prepare(equations, references, deltas);
		
		return solve(references, deltas);
	}
	
}
