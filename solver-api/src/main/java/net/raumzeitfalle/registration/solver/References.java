package net.raumzeitfalle.registration.solver;

public interface References {

	double[][] getArray();
	
	default int rows() {
		return getArray().length;
	}
}
