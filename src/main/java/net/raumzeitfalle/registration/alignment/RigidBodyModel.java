package net.raumzeitfalle.registration.alignment;

import java.util.Collection;

import net.raumzeitfalle.registration.Dimension;

public interface RigidBodyModel {
	RigidTransform solve(Collection<RigidModelEquation> equations, Dimension dimension);
}
