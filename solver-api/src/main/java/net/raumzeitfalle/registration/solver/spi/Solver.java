package net.raumzeitfalle.registration.solver.spi;

import java.util.function.BiFunction;

import net.raumzeitfalle.registration.solver.*;

@FunctionalInterface
public interface Solver extends BiFunction<References, Deltas, Solution> {

	public Solution solve(References t, Deltas u);
	
	@Override
	public default Solution apply(References t, Deltas u) {
		return solve(t, u);
	}
	
}
