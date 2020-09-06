package net.raumzeitfalle.registration.jblas;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;

import net.raumzeitfalle.registration.solver.Deltas;
import net.raumzeitfalle.registration.solver.References;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.Solutions;
import net.raumzeitfalle.registration.solver.spi.Solver;

public class JblasSolver implements Solver {

	public Solution solve(References references, Deltas deviations) {
		
		DoubleMatrix refs = new DoubleMatrix(references.getArray());
		DoubleMatrix deltas = new DoubleMatrix(deviations.getArray());
		DoubleMatrix coeffs = Solve.solveLeastSquares(refs, deltas);
		
		int rows = coeffs.getLength();

		double[] coefficients = new double[rows];
		for (int i = 0; i < rows; i++) {
			coefficients[i] = coeffs.get(i, 0);
		}
		
		return Solutions.fromArray(coefficients);		
	}

}
