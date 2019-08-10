package mask.registration.alignment;

import java.util.function.Function;

import mask.registration.Displacement;

/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 * 
 * @author oliver
 *
 */
public interface RigidTransform extends Function<Displacement, Displacement> {
	
	double getTranslationX();
	double getTranslationY();
	double getRotation();
	
}
