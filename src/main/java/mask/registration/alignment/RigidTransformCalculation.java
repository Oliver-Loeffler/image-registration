package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.displacement.Displacement;

/**
 * 
 * Calculates rotation and translation (x,y) for the given collection of displacements.
 * A rigid body transform will preserve all distances between points.
 *  
 * @author oliver
 *
 */
public class RigidTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, RigidTransform>{

	/**
	 * Calculates the alignment parameters (translation<sub>X</sub>, translation<sub>Y</sub> and rotation) for the given collection of displacements.
	 * When no displacements are left after filtering with the given predicate, then the {@link SkipRigidTransform} is returned. Otherwise the results is returned as {@link SimpleRigidTransform}.
	 * <p>
	 * The {@link RigidModel} is used for calculation.
	 * <p>
	 * For cases where no sites are selected for calculation (e.g. the predicate does not match any given displacement),
	 * A {@link SkipRigidTransform} is returned instead of a {@link SimpleRigidTransform}. When used in data processing, 
	 * the {@link SkipRigidTransform} will just pass through all given data without any modification.
	 * 
	 * @param t Collection of {@link Displacement}
	 * @param u {@link Predicate} which describes which {@link Displacement} elements shall be used for alignment
	 * @return {@link RigidTransform} providing translation (x,y) and rotation values.
	 */
	@Override
	public RigidTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
						
		
		List<RigidModelEquation> equations = t.stream()
											 .filter(u)
											 .flatMap(RigidModelEquation::from)
											 .collect(Collectors.toList());
		
		if (equations.isEmpty()) {
			return continueUnaligned();
		}
		
		RigidModel model = new RigidModel(equations);
		
		Matrix result = model.solve();
		
        double translationX = result.get(0, 0);
        double translationY = result.get(1, 0);
        double rotation = result.get(2, 0);
        
        return SimpleRigidTransform
        		.with(translationX, translationY, rotation);
	}

	private RigidTransform continueUnaligned() {
		return new SkipRigidTransform();
	}

	

}
