package mask.registration.firstorder;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import Jama.Matrix;
import Jama.QRDecomposition;

public class FirstOrderModel {

	private final int rows;
	
	private final Matrix references;
	
	private final Matrix deltas;
	
    public FirstOrderModel(Collection<FirstOrderEquation> equations) {
    	this.rows = equations.size();
    	this.references = new Matrix(this.rows, 4);
    	this.deltas = new Matrix(this.rows, 1);
    	
    	populateMatrices(equations.toArray(new FirstOrderEquation[0]));
    }
    
    private void populateMatrices(FirstOrderEquation[] equations) {
    	for (int m = 0; m < equations.length; m++) {
    		FirstOrderEquation eq = equations[m];
			this.references.set(m, 0, eq.getSx());
			this.references.set(m, 1, eq.getSy());
			this.references.set(m, 2, eq.getOx());
			this.references.set(m, 3, eq.getOy());
//			this.references.set(m, 4, eq.getTx());
//			this.references.set(m, 5, eq.getTy());
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
		return "FirstOrderModel [" + System.lineSeparator() +  showMatrix(references) 
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
