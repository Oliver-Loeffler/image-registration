package net.raumzeitfalle.registration.solver;

/**
 * A matrix of design locations.
 * The design desribes where features are expected to be located. 
 *
 */
public interface References {

    /**
     * @return A 2D array describing the X-Y-location of design locations.
     */
	double[][] getArray();
	
	/**
	 * @return The data set size, usually the number of measurements obtained. 
	 */
	default int rows() {
		return getArray().length;
	}
}
