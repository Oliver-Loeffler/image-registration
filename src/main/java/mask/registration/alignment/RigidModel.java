package mask.registration.alignment;

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
public class RigidModel {
	
	private final int rows;
	private final Matrix references;
	private final Matrix deltas;
	
	public RigidModel(Collection<RigidModelEquation> equations) {
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
