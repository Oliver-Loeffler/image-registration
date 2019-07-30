package mask.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlignmentCorrection implements Function<RigidTransform, List<Displacement>> {
	
	private final Collection<Displacement> t;
	
	AlignmentCorrection(Collection<Displacement> t) {
		this.t = t;
	}
	
	@Override
	public List<Displacement> apply(RigidTransform u) {

		return t.stream()
				.map(u)
				.collect(Collectors.toList());
		
	}
	
	

}
