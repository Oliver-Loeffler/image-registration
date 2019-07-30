package mask.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mask.registration.alignment.AlignmentTransform;

public class TranslationCorrection implements BiFunction<Collection<Displacement>,Predicate<Displacement>, List<Displacement>> {

	@Override
	public List<Displacement> apply(Collection<Displacement> t, Predicate<Displacement> u) {
		
		double residualMeanX = Displacement.average(t, u, Displacement::dX);
		double residualMeanY = Displacement.average(t, u, Displacement::dY);
	
		AlignmentTransform transform = AlignmentTransform.with(residualMeanX, residualMeanY, 0.0);
		
		List<Displacement> transformed = t.stream()
									      .map(transform::apply)
									      .collect(Collectors.toList());
		
		return transformed;
	}

	

}
