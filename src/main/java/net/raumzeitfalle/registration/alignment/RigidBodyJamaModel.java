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
import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.Orientable;

/**
 * 
 * Calculates translation and rotation for the given set of equations. A rigid
 * transform preserves all distances between points.
 *
 * <ul>
 * <li>Translation</li>
 * <li>Rotation</li>
 * </ul>
 *
 * @author oliver
 * 
 */
final class RigidBodyJamaModel implements RigidBodyModel {

	private void prepare(Collection<RigidModelEquation> equations, Matrix references, Matrix deltas, Orientation direction) {
		int row = 0;
		Iterator<RigidModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(Matrix references, Matrix deltas, int row, RigidModelEquation eq, Orientation direction) {

		if (Orientation.X.equals(direction)) {
			references.set(row, 0, eq.getXf());
			references.set(row, 1, eq.getDesignValue());
		}
		
		if (Orientation.Y.equals(direction)) {
			references.set(row, 0, eq.getYf());
			references.set(row, 1, eq.getDesignValue());
		}
		
		if (Orientation.XY.equals(direction)) {
			references.set(row, 0, eq.getXf());
			references.set(row, 1, eq.getYf());
			references.set(row, 2, eq.getDesignValue());
		}

		deltas.set(row, 0, eq.getDeltaValue());
		row++;
		return row;
	}

	private RigidTransform solve(Matrix references, Matrix deltas, Orientation direction) {

		QRDecomposition qr = new QRDecomposition(references);
		
		Matrix rInverse = qr.getR().inverse();
		Matrix qTransposed = qr.getQ().transpose();
		Matrix solved = rInverse.times(qTransposed).times(deltas);
		
		return createTransform(solved,direction);
	}

	private RigidTransform createTransform(Matrix solved, Orientation direction) {
		
		if (Orientation.X.equals(direction)) {
			return SimpleRigidTransform
					.with(solved.get(0, 0), 0.0, solved.get(1, 0));
		}
		
		if (Orientation.Y.equals(direction)) {
			return SimpleRigidTransform
					.with(0.0, solved.get(0, 0), solved.get(1, 0));
		}
		
		return SimpleRigidTransform
				.with(solved.get(0, 0), solved.get(1, 0), solved.get(2, 0));
	}

	@Override
	public <T extends Orientable> RigidTransform solve(Collection<RigidModelEquation> equations, Dimension<T> dimension) {
		
		int rows = equations.size();
		int cols = dimension.getDimensions()+1;
		
		Orientation direction = dimension.getDirection();
		
		Matrix deltas = new Matrix(rows, 1);
		Matrix references = new Matrix(rows, cols);
		prepare(equations, references, deltas, direction);
		
		// escape here before singular matrix exception can be thrown
		if (Orientation.XY.equals(direction) && equations.size() == 2) {
			double tx = deltas.get(0, 0);
			double ty = deltas.get(1, 0);
			return SimpleRigidTransform.translation(tx, ty);
		}
		
		if (Orientation.X.equals(direction) && equations.size() == 1) {
			double tx = deltas.get(0, 0);
			return SimpleRigidTransform.shiftX(tx);
		}
		
		if (Orientation.Y.equals(direction) && equations.size() == 1) {
			double ty = deltas.get(0, 0);
			return SimpleRigidTransform.shiftY(ty);
		}
				
		return solve(references, deltas, direction);
	}
	
}
