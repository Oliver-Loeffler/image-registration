package net.raumzeitfalle.registration.solver;

import net.raumzeitfalle.registration.ModelEquation;

public final class DeltasImpl {

	private final double[] deltas;
	
	public DeltasImpl(int rows) {
		this.deltas = new double[rows];
	}

	public final double[] getArray() {
		return this.deltas;
	}

	protected final void set(int row, double deltaValue) {
		this.deltas[row] = deltaValue;
	}
	
	public final void set(int row, ModelEquation eq) {
		set(row, eq.getDeltaValue());
	}

	public final double get(int row) {
		return this.deltas[row];
	}

	public final int rows() {
		return this.deltas.length;
	}
	
}
