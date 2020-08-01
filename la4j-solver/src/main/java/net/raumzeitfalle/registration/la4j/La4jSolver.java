package net.raumzeitfalle.registration.la4j;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.Matrix;
import org.la4j.decomposition.QRDecompositor;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.Solver;


class La4jSolver implements Solver {

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
