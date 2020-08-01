package net.raumzeitfalle.registration.solver;

public final class Solution {
	
	private final double[] coefficients;
	
	public Solution(double[] coefficients) {
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
