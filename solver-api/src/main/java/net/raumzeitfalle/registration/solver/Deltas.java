package net.raumzeitfalle.registration.solver;

public interface Deltas {

	double[] getArray();

	default int rows() {
		return getArray().length;
	}
	
}
