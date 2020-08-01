package net.raumzeitfalle.registration.ejml;

import org.ejml.simple.SimpleMatrix;

import net.raumzeitfalle.registration.solver.*;

class EjmlSolver implements Solver {

	public Solution solve(References references, Deltas deviations) {
		
		SimpleMatrix refs = new SimpleMatrix(references.getArray());
		SimpleMatrix deltas = new SimpleMatrix(deviations.rows(), 1, true, deviations.getArray());
		
		SimpleMatrix coeffs = refs.solve(deltas);
		
		int rows = coeffs.numRows();
		double[] coefficients = new double[coeffs.numRows()];
		
		for (int i = 0; i < rows; i++) {
			coefficients[i] = coeffs.get(i, 0);
		}
		
		return new Solution(coefficients);
		
	}

}
