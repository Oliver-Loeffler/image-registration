package net.raumzeitfalle.registration.solver;

final class SolutionVector implements Solution {
	
	private final double[] coefficients;
	
    SolutionVector(double[] coefficients) {
		this.coefficients = coefficients;
	}
	
	public final double get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be > or = to zero.");
		}
		
		if (index > this.coefficients.length && this.coefficients.length > 0) {
			throw new IllegalArgumentException("index must be >= 0 and < " + this.coefficients.length);
		}
		
		if (index > this.coefficients.length) {
			throw new IllegalArgumentException("Solution is empty, no coefficients available.");
		}
		
		return this.coefficients[index];
	}
}
