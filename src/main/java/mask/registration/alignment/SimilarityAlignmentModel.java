package mask.registration.alignment;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import Jama.Matrix;
import Jama.QRDecomposition;

/**
 * 
 * Calculates rotation, translation (x,y) and anisotropic scale (scale x,y) for the given collection of equations.
 * This transform will not preserve angles and distances.
 * 
 * <ul>
 * 	<li>Translation (x,y)</li>
 *  <li>Rotation</li>
 *  <li>Non-uniform scale (scale<sub>X</sub> and scale<sub>Y</sub>)</li>
 * </ul>
 *  
 * @author oliver
 *
 */
// TODO: find a better name, similarity seems to be wrong
public class SimilarityAlignmentModel {
	
	private final int rows;
	
	private final Matrix references;
	
	private final Matrix deltas;
	
	public SimilarityAlignmentModel(Collection<SimilarityAlignmentModelEquation> equations) {
    	this.rows = equations.size();
    	this.references = new Matrix(this.rows, 5);
    	this.deltas = new Matrix(this.rows, 1);
    	
    	populateMatrices(equations.toArray(new SimilarityAlignmentModelEquation[0]));
    }
    
    private void populateMatrices(SimilarityAlignmentModelEquation[] equations) {
    	for (int m = 0; m < equations.length; m++) {
    		SimilarityAlignmentModelEquation eq = equations[m];
			this.references.set(m, 0, eq.getSx());
			this.references.set(m, 1, eq.getSy());
			this.references.set(m, 2, eq.getRot());
			this.references.set(m, 3, eq.getTx());
			this.references.set(m, 4, eq.getTy());
			
			deltas.set(m, 0, eq.getDeltaValue());
		}
	}

    public Matrix solve() {
		QRDecomposition qr = new QRDecomposition(references);
		Matrix Rinverse = qr.getR().inverse();
		Matrix Qtransposed = qr.getQ().transpose();
        return Rinverse.times(Qtransposed).times(deltas);
	}

	@Override
	public String toString() {
		return "SimilarityAlignmentModel [" + System.lineSeparator() +  showMatrix(references) 
				+ System.lineSeparator()
				+ "]";
	}
    
	
	private String showMatrix(Matrix matrix) {
		int rows = matrix.getRowDimension();
		
		StringBuilder b = new StringBuilder();
		double[][] array = matrix.getArray();
		for (int m = 0; m < rows; m++) {
			b.append(Arrays.stream(array[m]).mapToObj(this::format).collect(Collectors.joining(", ", "(", ")")));
			b.append(" => (").append(format(deltas.get(m, 0),6)).append(")").append(System.lineSeparator());
		}
		
		return b.toString();
	}
    
	private String format(double value) {
		return format(value, 3);
	}
	
	private String format(double value, int digits) {
		return String.format("%."+digits+"f", value);
	}

}
