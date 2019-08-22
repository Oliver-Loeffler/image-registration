/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Jama.Matrix;
import net.raumzeitfalle.registration.displacement.Displacement;

public final class NonUniformSimilarityCalculation
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
