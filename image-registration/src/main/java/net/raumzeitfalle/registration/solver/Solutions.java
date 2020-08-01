package net.raumzeitfalle.registration.solver;

public class Solutions {
	
	private Solutions() {
		// not intended to be instantiated
	}
	
	public static Solution fromArray(double[] vector) {
		return new SolutionVector(vector);
	}
	
}
