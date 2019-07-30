package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import mask.registration.Displacement;

public class AlignmentCorrection implements BiFunction<AlignmentTransform, Collection<Displacement>, List<Displacement>> {
		
	@Override
	public List<Displacement> apply(AlignmentTransform t, Collection<Displacement> u) {

		return u.stream()
				.map(t)
				.collect(Collectors.toList());
		
	}
	
	

}
