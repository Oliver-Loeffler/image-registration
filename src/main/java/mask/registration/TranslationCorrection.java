package mask.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TranslationCorrection implements Function<Collection<Displacement>, List<Displacement>> {

	@Override
	public List<Displacement> apply(Collection<Displacement> t) {
		
		double residualMeanX = Displacement.average(t, Displacement::dX);
		double residualMeanY = Displacement.average(t, Displacement::dY);
	
		RigidTransform transform = RigidTransform.with(residualMeanX, residualMeanY, 0.0);
		
		List<Displacement> transformed = t.stream()
									      .map(transform::apply)
									      .collect(Collectors.toList());
		
		return transformed;
	}

	

}
