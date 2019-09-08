package net.raumzeitfalle.registration.alignment;

import java.util.Collection;

import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.Orientable;

public interface RigidBodyModel {
	<T extends Orientable> RigidTransform solve(Collection<RigidModelEquation> equations, Dimension<T> dimension);
}
