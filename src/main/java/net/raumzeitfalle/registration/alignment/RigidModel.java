/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;

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
final class RigidModel {
	
	private final int rows;
	private final Matrix references;
	private final Matrix deltas;
	
	public RigidModel(Collection<RigidModelEquation> equations) {
		
		/* 
		 * TODO: Depending on equations, the model should decide to include or exclude one dimension
		 * or instead to populate one axis fully with 0.0.
		 * 
		 */
		
		this.rows = equations.size();
		this.references = new Matrix(this.rows, 3);
		this.deltas = new Matrix(this.rows, 1);
		
		populateMatrices(equations.toArray(new RigidModelEquation[0]));
	}

	private void populateMatrices(RigidModelEquation[] equations) {
		for (int m = 0; m < equations.length; m++) {
			RigidModelEquation eq = equations[m];
			this.references.set(m, 0, eq.getXf());
			this.references.set(m, 1, eq.getYf());
			this.references.set(m, 2, eq.getDesignValue());
			deltas.set(m, 0, eq.getDeltaValue());
		}
	}
	
	public Matrix solve() {
		QRDecomposition qr = new QRDecomposition(references);
		Matrix Rinverse = qr.getR().inverse();
		Matrix Qtransposed = qr.getQ().transpose();
        return Rinverse.times(Qtransposed).times(deltas);
	}
}
