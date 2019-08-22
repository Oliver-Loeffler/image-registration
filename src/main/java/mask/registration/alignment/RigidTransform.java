package mask.registration.alignment;

import java.util.Collection;
import java.util.function.Function;

import mask.registration.displacement.Displacement;

/**
 * 
 * Allows to correct rotation and translation (x,y) for a given displacement.
 * 
 * @author oliver
 *
 */
public interface RigidTransform extends Function<Collection<Displacement>, Collection<Displacement>> {
	
	double getTranslationX();
	double getTranslationY();
	double getRotation();
	
}
