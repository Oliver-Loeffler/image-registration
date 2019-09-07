package net.raumzeitfalle.registration.alignment;

import java.util.Collection;

public interface RigidBodyModel {
	RigidTransform solve(Collection<RigidModelEquation> equations);
}
