package net.raumzeitfalle.registration.solver;

/**
 * A vector holding the coefficients obtained by solving the described system of equations.
 * The length of the coefficient vector depends on the degrees of freedom of the data set used for calculation.
 *
 */
public interface Solution {
	double get(int index);
}
