package net.raumzeitfalle.registration.alignment;

import java.util.Objects;

import net.raumzeitfalle.registration.DifferencesVector;
import net.raumzeitfalle.registration.OrientedOperation;

class RigidTransformTranslationsFactory implements OrientedOperation<RigidTransform> {
	
	private final DifferencesVector solution;
	
	public RigidTransformTranslationsFactory(DifferencesVector differences) {
		this.solution = Objects.requireNonNull(differences, "differences must never be null");
	}

	@Override
	public RigidTransform getX() {
		return RigidTransform.shiftX(solution.get(0));
	}

	@Override
	public RigidTransform getY() {
		return RigidTransform.shiftY(solution.get(0));
	}

	@Override
	public RigidTransform getCombined() {
		return RigidTransform.translation(solution.get(0), solution.get(1));
	}

}
