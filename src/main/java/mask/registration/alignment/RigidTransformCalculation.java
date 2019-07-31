package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;

/**
 * 
 * Calculates rotation and translation (x,y) for the given collection of displacements.
 * A rigid body transform will preserve all distances between points.
 *  
 * @author oliver
 *
 */
public class RigidTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, RigidTransform>{

	@Override
	public RigidTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
						
		List<RigidModelEquation> equations = t.stream()
											 .filter(u)
											 .flatMap(RigidModelEquation::from)
											 .collect(Collectors.toList());
		
		RigidModel model = new RigidModel(equations);
		
		Matrix result = model.solve();
		
        double translationX = result.get(0, 0);
        double translationY = result.get(1, 0);
        double rotation = result.get(2, 0);
        
        return RigidTransform.with(translationX, translationY, rotation);
	}

	

}
