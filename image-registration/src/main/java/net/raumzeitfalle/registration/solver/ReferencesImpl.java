package net.raumzeitfalle.registration.solver;

import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.alignment.RigidModelEquation;
import net.raumzeitfalle.registration.distortions.AffineModelEquation;

public final class ReferencesImpl {
	
	private final double[][] matrix;
	
	public ReferencesImpl(int rows, int cols) {
		this.matrix = new double[rows][cols];
	}

	public final double[][] getArray() {
		return this.matrix;
	}

	public final void set(int row, int column, double value) {
		this.matrix[row][column] = value;
	}
	
	public final void set(int row, RigidModelEquation eq, Orientation direction) {
		if (Orientation.X.equals(direction)) {
			set(row, 0, eq.getXf());
			set(row, 1, eq.getDesignValue());
		}
		
		if (Orientation.Y.equals(direction)) {
			set(row, 0, eq.getYf());
			set(row, 1, eq.getDesignValue());
		}
		
		if (Orientation.XY.equals(direction)) {
			set(row, 0, eq.getXf());
			set(row, 1, eq.getYf());
			set(row, 2, eq.getDesignValue());
		}
	}
	
	public final void set2D(int row, AffineModelEquation eq, Orientation direction) {
		if (Orientation.X.equals(direction)) {
			set(row, 0, eq.getSx());
			set(row, 1, eq.getOy());
			set(row, 2, eq.getTx());
		}
		
		if (Orientation.Y.equals(direction)) {
			set(row, 0, eq.getSy());
			set(row, 1, eq.getOx());
			set(row, 2, eq.getTy());
		}
		
		if (Orientation.XY.equals(direction)) {
			set(row, 0, eq.getSx());
			set(row, 1, eq.getSy());
			set(row, 2, eq.getOx());
			set(row, 3, eq.getOy());
			set(row, 4, eq.getTx());
			set(row, 5, eq.getTy());
		}
	}
	
	public final void set1D(int row, AffineModelEquation eq, Orientation direction) {
		if (eq.matches(Orientation.X)) {
			set(row, 0, eq.getSx());
			set(row, 1, eq.getOy());
			set(row, 2, eq.getTx());
			set(row, 3, eq.getTy());
		}
		
		if (eq.matches(Orientation.Y)) {
			set(row, 0, eq.getSy());
			set(row, 1, eq.getOx());
			set(row, 2, eq.getTx());
			set(row, 3, eq.getTy());
		}
	}
}
