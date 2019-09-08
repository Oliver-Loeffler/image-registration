package net.raumzeitfalle.registration;

import java.util.Collection;
import java.util.function.Function;

import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * Transforms are used to modify the actual displacement values (x<sub>d</sub>, y<sub>d</sub>). The reference (design) locations (x,y) remain unmodified.
 * A transform applies to {@link Displacement} and returns a {@link Displacement}.
 * <p>
 * To properly apply any given {@link Transform} to a {@link Collection} of displacements, the {@link Function} {@link TransformCorrection} shall be used.
 * {@code
 * 
 * 
 * }
 * @author oliver
 *
 */
public interface Transform extends Function<Displacement, Displacement> {
	
	/**
	 * When the relevant transform parameters (i.e. rotation, translation-x and y) are set to 0.0,
	 * then the transform calculation can be skipped. Transforms shall only be applied, when transform
	 * coefficients are not equal to zero.
	 * 
	 * @return boolean indicating if the given transform can be skipped.
	 */
	boolean skip();
	
}
