package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import mask.registration.Displacement;

public class RigidCorrection implements BiFunction<RigidTransform, Collection<Displacement>, List<Displacement>> {
		
	@Override
	public List<Displacement> apply(RigidTransform t, Collection<Displacement> u) {

		return u.stream()
				.map(t)
				.collect(Collectors.toList());
		
	}
	
	

}
