package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;
import Jama.QRDecomposition;
import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.Distribution;
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.SpatialDistribution;

class JamaOneDimensionalAffineModel implements AffineModel {
	
	private final Distribution spatialOrientation;
	
	JamaOneDimensionalAffineModel(SpatialDistribution distribution) {
		this.spatialOrientation = distribution.getDistribution();
	}

	@Override
	public <T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations,
			Dimension<T> dimension) {
		
		/*
		 * Assumption:
		 *  all data points distributed along one vertical line
		 *  different Y locations, exactly one X location
		 *  => this would render the normal affine model to fail 
		 *     with a singular matrix  
		 * 
		 *  What can be calculated?
		 *   1. translation-x
		 *   2. translation-y
		 *   3. scale-y
		 *   4. ortho-y (aka. rotation in this case)
		 *  
		 *  What can't be calcutaled? 
		 *   1. scale-x (this would require multiple design x-locations)
		 *   
		 *  Same applies in case of data distributed along one horizontal
		 *  line.
		 * 
		 */
		
		int cols = 4; // dimension.getDimensions() * 2;  
		int rows = equations.size();
		
		Matrix references = new Matrix(rows, cols);
		Matrix deltas = new Matrix(rows, 1);

		Orientation direction = dimension.getDirection();

		prepare(equations, references, deltas, direction);
		
		return solve(references, deltas, direction);
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
		
		if (eq.matches(Orientation.X)) {
			references.set(row, 0, eq.getSx());
			references.set(row, 1, eq.getOy());
			references.set(row, 2, eq.getTx());
			references.set(row, 3, eq.getTy());
		}
		
		if (eq.matches(Orientation.Y)) {
			references.set(row, 0, eq.getSy());
			references.set(row, 1, eq.getOx());
			references.set(row, 2, eq.getTx());
			references.set(row, 3, eq.getTy());
		}

		deltas.set(row, 0, eq.getDeltaValue());
		row++;
		return row;
		
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
		
		double scale = solved.get(0, 0);
	    double rot = solved.get(1, 0);
	    double tx = solved.get(2, 0);
	    double ty = solved.get(3, 0);
	    
		if (Distribution.HORIZONTAL.equals(spatialOrientation)) {
			return SimpleAffineTransform.horizontal(tx, ty, scale, rot);
		}
		
		if (Distribution.VERTICAL.equals(spatialOrientation)) {
			return SimpleAffineTransform.vertical(tx, ty, scale, rot);
		}
		
		String message = String.format("Evaluation of data with %s spatial orientation not supported.", spatialOrientation);
		throw new UnsupportedOperationException(message);
	}

}
