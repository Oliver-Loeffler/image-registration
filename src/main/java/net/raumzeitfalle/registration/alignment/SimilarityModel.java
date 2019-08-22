package net.raumzeitfalle.registration.alignment;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import Jama.Matrix;
import Jama.QRDecomposition;

/**
 * 
 * Calculates rotation, translation (x,y) and isotropic scale (aka. magnification) for the given collection of equations.
 * A similarity transformation will preserve all angles between vectors.
 * 
 * <ul>
 * 	<li>Translation</li>
 *  <li>Rotation</li>
 *  <li>Uniform scale (Magnification)</li>
 * </ul>
 *  
 * @author oliver
 *
 */
public class SimilarityModel {
	
	private final int rows;
	
	private final Matrix references;
	
	private final Matrix deltas;
	
	public SimilarityModel(Collection<SimilarityModelEquation> equations) {
    	this.rows = equations.size();
    	this.references = new Matrix(this.rows, 4);
    	this.deltas = new Matrix(this.rows, 1);
    	
    	populateMatrices(equations.toArray(new SimilarityModelEquation[0]));
    }
    
    private void populateMatrices(SimilarityModelEquation[] equations) {
    	for (int m = 0; m < equations.length; m++) {
    		SimilarityModelEquation eq = equations[m];
			this.references.set(m, 0, eq.getMag());
			this.references.set(m, 1, eq.getRot());
			this.references.set(m, 2, eq.getTx());
			this.references.set(m, 3, eq.getTy());
			
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
		return "SimilarityModel [" + System.lineSeparator() +  showMatrix(references) 
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
