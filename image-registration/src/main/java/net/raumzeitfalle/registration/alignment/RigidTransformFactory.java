package net.raumzeitfalle.registration.alignment;

import java.util.Objects;

import net.raumzeitfalle.registration.OrientedOperation;
import net.raumzeitfalle.registration.solver.Solution;

class RigidTransformFactory implements OrientedOperation<RigidTransform> {
	
	private final Solution solution;
	
	public RigidTransformFactory(Solution solved) {
		this.solution = Objects.requireNonNull(solved, "solved must never be null");
	}

	@Override
	public RigidTransform getX() {
		return RigidTransform.with(solution.get(0), 0.0, solution.get(1));
	}

	@Override
	public RigidTransform getY() {
		return RigidTransform.with(0.0, solution.get(0), solution.get(1));
	}

	@Override
	public RigidTransform getCombined() {
		return RigidTransform.with(solution.get(0), solution.get(1), solution.get(2));
	}

}
