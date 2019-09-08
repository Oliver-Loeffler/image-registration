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
package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;
import Jama.QRDecomposition;
import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.Orientation;

/**
 * 
 * Calculates translation (x,y) and scale (x,y) and non-orthogonality (shear, x,y) for the given collection of equations.
 * An affine transform preserves parallelism.
 * 
 * <ul>
 * 	<li>Translation</li>
 *  <li>Rotation</li>
 *  <li>Scale</li>
 *  <li>Shear</li>
 * </ul>
 *  
 * @author oliver
 *
 */
final class JamaAffineModel implements AffineModel {

	@Override
	public <T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations,
			Dimension<T> dimension) {
		
		// there are 3 coefficients per direction
		int cols = dimension.getDimensions() * 3;
		int rows = equations.size();
		
		Matrix references = new Matrix(rows, cols);
		Matrix deltas = new Matrix(rows, 1);
		
		Orientation direction = dimension.getDirection();
		prepare(equations, references, deltas, direction);
		
		return solve(references, deltas, direction);
	}

	private AffineTransform solve(Matrix references, Matrix deltas, Orientation direction) {
		QRDecomposition qr = new QRDecomposition(references);
		Matrix Rinverse = qr.getR().inverse();
		Matrix Qtransposed = qr.getQ().transpose();
		
		Matrix solved = Rinverse.times(Qtransposed)
				                .times(deltas);
		
		return createTransform(solved, direction);
	}

	private AffineTransform createTransform(Matrix solved, Orientation direction) {
		
		double zero = 0d;
		
		if (Orientation.X.equals(direction)) {
			double tx = solved.get(2, 0);
			double ox = solved.get(1, 0);
			double sx = solved.get(0, 0);	
			return new SimpleAffineTransform(tx, zero, sx, zero, ox, zero, zero, zero);
		}
		
		if (Orientation.Y.equals(direction)) {
			double ty = solved.get(2, 0);
			double oy = solved.get(1, 0);
			double sy = solved.get(0, 0);
			return new SimpleAffineTransform(zero, ty, zero, sy, zero, oy, zero, zero);
		}
		
		double sx = solved.get(0, 0);
	    double sy = solved.get(1, 0);
	    double ox = solved.get(2, 0);
	    double oy = solved.get(3, 0);
	    double tx = solved.get(4, 0);
	    double ty = solved.get(5, 0);

		return new SimpleAffineTransform(tx, ty, sx, sy, ox, oy, zero, zero);
	}

	private void prepare(Collection<AffineModelEquation> equations, Matrix references, Matrix deltas,
			Orientation direction) {
		int row = 0;
		Iterator<AffineModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(Matrix references, Matrix deltas, int row, AffineModelEquation eq,
			Orientation direction) {

		if (Orientation.X.equals(direction)) {
			references.set(row, 0, eq.getSx());
			references.set(row, 1, eq.getOy());
			references.set(row, 2, eq.getTx());
		}
		
		if (Orientation.Y.equals(direction)) {
			references.set(row, 0, eq.getSy());
			references.set(row, 1, eq.getOx());
			references.set(row, 2, eq.getTy());
		}
		
		if (Orientation.XY.equals(direction)) {
			references.set(row, 0, eq.getSx());
			references.set(row, 1, eq.getSy());
			references.set(row, 2, eq.getOx());
			references.set(row, 3, eq.getOy());
			references.set(row, 4, eq.getTx());
			references.set(row, 5, eq.getTy());
		}

		deltas.set(row, 0, eq.getDeltaValue());
		row++;
		
		return row;
	}

}
