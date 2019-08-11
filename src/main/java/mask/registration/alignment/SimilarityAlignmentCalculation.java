package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import mask.registration.Displacement;

public class SimilarityAlignmentCalculation
		implements BiFunction<Collection<Displacement>, Predicate<Displacement>, SimilarityAlignmentTransform> {

	@Override
	public SimilarityAlignmentTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
		List<SimilarityAlignmentModelEquation> equations = t.stream()
				.filter(u)
				.flatMap(SimilarityAlignmentModelEquation::from)
				.collect(Collectors.toList());


		SimilarityAlignmentModel model = new SimilarityAlignmentModel(equations);

		Matrix result = model.solve();

		double scaleX = result.get(0, 0);
		double scaleY = result.get(1, 0);
		double rotation = result.get(2, 0);
		double translationX = result.get(3, 0);
		double translationY = result.get(4, 0);

		return SimilarityAlignmentTransform.with(scaleX, scaleY, translationX, translationY, rotation);
	}

}
