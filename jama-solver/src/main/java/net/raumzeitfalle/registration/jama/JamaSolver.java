package net.raumzeitfalle.registration.jama;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.Solver;

import Jama.Matrix;
import Jama.QRDecomposition;

public class JamaSolver implements Solver {
	
	public Solution solve(References references, Deltas deviations) {
		
		Matrix refs = new Matrix(references.getArray());
		Matrix deltas = new Matrix(deviations.getArray(), deviations.rows());
		
		QRDecomposition qr = new QRDecomposition(refs);
		
		Matrix rInverse = qr.getR().inverse();
		Matrix qTransposed = qr.getQ().transpose();
		Matrix solution = rInverse.times(qTransposed)
								  .times(deltas);
		
		return Solutions.fromArray(solution.getColumnVector(0));
		
	}

	@Override
	public Solution apply(References t, Deltas u) {
		return solve(t, u);
	}
}
