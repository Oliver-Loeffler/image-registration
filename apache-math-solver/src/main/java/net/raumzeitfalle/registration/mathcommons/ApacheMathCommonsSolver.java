package net.raumzeitfalle.registration.mathcommons;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import net.raumzeitfalle.registration.solver.*;
import net.raumzeitfalle.registration.solver.spi.Solver;


class ApacheMathCommonsSolver implements Solver {

	public Solution solve(References references, Deltas deviations) {
		
		RealMatrix refs = MatrixUtils.createRealMatrix(references.getArray());
		RealMatrix deltas = MatrixUtils.createColumnRealMatrix(deviations.getArray());
		
		QRDecomposition qr = new QRDecomposition(refs);
	
		RealMatrix rInverse = createInverse(qr);
		
		RealMatrix qTransposed = qr.getQ().transpose();
		
		RealMatrix solution = rInverse.multiply(qTransposed)
				                      .multiply(deltas);
		
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
