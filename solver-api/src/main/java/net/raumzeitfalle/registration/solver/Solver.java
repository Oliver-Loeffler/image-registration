package net.raumzeitfalle.registration.solver;

import java.util.function.BiFunction;

public interface Solver extends BiFunction<References, Deltas, Solution> {

	public Solution solve(References t, Deltas u);
	
	@Override
	public default Solution apply(References t, Deltas u) {
		return solve(t, u);
	}
	
}
