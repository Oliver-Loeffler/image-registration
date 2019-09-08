package net.raumzeitfalle.registration.distortions;

import java.util.Collection;

import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.Orientable;

public interface AffineModel {
	<T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations, Dimension<T> dimension);
}
