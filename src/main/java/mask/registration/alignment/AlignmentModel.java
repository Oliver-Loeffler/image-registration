package mask.registration.alignment;

import java.util.Collection;

import Jama.Matrix;
import Jama.QRDecomposition;

class AlignmentModel {
	
	private final int rows;
	private final Matrix references;
	private final Matrix deltas;
	
	public AlignmentModel(Collection<AlignmentEquation> equations) {
		this.rows = equations.size();
		this.references = new Matrix(this.rows, 3);
		this.deltas = new Matrix(this.rows, 1);
		
		populateMatrices(equations.toArray(new AlignmentEquation[0]));
	}

	private void populateMatrices(AlignmentEquation[] equations) {
		for (int m = 0; m < equations.length; m++) {
			AlignmentEquation eq = equations[m];
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
