package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import net.raumzeitfalle.registration.displacement.Displacement;

public class NonUniformSimilarityCalculation
		implements BiFunction<Collection<Displacement>, Predicate<Displacement>, NonUniformSimilarityTransform> {

	@Override
	public NonUniformSimilarityTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {
		List<NonUniformSimilarityModelEquation> equations = t.stream()
				.filter(u)
				.flatMap(NonUniformSimilarityModelEquation::from)
				.collect(Collectors.toList());


		NonUniformSimilarityModel model = new NonUniformSimilarityModel(equations);

		Matrix result = model.solve();

		double scaleX = result.get(0, 0);
		double scaleY = result.get(1, 0);
		double rotation = result.get(2, 0);
		double translationX = result.get(3, 0);
		double translationY = result.get(4, 0);

		return NonUniformSimilarityTransform.with(scaleX, scaleY, translationX, translationY, rotation);
	}

}
