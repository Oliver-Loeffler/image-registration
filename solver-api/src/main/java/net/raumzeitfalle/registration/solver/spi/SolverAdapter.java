package net.raumzeitfalle.registration.solver.spi;

import java.util.function.BiFunction;

import net.raumzeitfalle.registration.solver.*;

/**
 * As Image Registration does not provide the actual required linear algebra (LA) implementations,
 * this interfaces serves as the adapter to connect utilize external LA libraries.
 * <p>
 * There is a number of example implementations available for some popular libraries. 
 * 
 */
@FunctionalInterface
public interface SolverAdapter extends BiFunction<References, Deltas, Solution> {

    /**
     * Here the actual logic for image registration calculation shall be implemented.
     * As the Image Registration library is supposed to run all checks and to ensure that a result can be calculated, there is no exception handling expected here.
     * All data passed into this method call is expected to represent solvable systems of equations.
     * 
     * @param t {@link References} = design locations
     * @param u Deltas = deviations of actual positions to the expected design positions
     * @return {@link Solution} as a vector with the coefficients of the model, where the length of the vector depends on the size of the given design and deviation matrices.
     */
	public Solution solve(References t, Deltas u);
	
	/**
     * Default implementation so that this class is compatible with {@link BiFunction} interface.
     * Internally the {@code solve(..,..)} method is called.
     * 
     * @param t {@link References} = design locations
     * @param u Deltas = deviations of actual positions to the expected design positions
     * @return {@link Solution} as a vector with the coefficients of the model, where the length of the vector depends on the size of the given design and deviation matrices.
     */
	@Override
	public default Solution apply(References t, Deltas u) {
		return solve(t, u);
	}
	
}
