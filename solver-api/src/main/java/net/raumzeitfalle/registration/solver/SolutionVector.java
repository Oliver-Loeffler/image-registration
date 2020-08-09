package net.raumzeitfalle.registration.solver;

import java.util.Objects;

final class SolutionVector implements Solution {
	
	private final double[] coefficients;
	
    SolutionVector(double[] coefficients) {
		this.coefficients = Objects.requireNonNull(coefficients, "array of solution coefficients must not be null");
	}
	
	public final double get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be > or = to zero");
		}
		
		if (index > this.coefficients.length && this.coefficients.length > 0) {
			throw new IllegalArgumentException("index must be in intervall [0, " + (this.coefficients.length-1)+"]");
		}
		
		if (index > this.coefficients.length) {
			throw new IllegalArgumentException("The solution is empty, no coefficients available.");
		}
		
		return this.coefficients[index];
	}
}
