/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.alignment.TranslateFunction;
import net.raumzeitfalle.registration.displacement.Displacement;

public final class CenteredAffineTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, AffineTransform> {

	private final AffineModel model;
	
	public CenteredAffineTransformCalculation() {
		this(new JamaAffineModel());
	}
	
	public CenteredAffineTransformCalculation(AffineModel model) {
		this.model = Objects.requireNonNull(model, "The used model for calculation must not be null (AffineModel).");
	}
	
	@Override
	public AffineTransform apply(Collection<Displacement> t, Predicate<Displacement> u) {

    	TranslateFunction translate = Displacement.translationToCenter(t, u);
    	
    	Dimension<AffineModelEquation> dimension = new Dimension<>();
    	
    	List<AffineModelEquation> finalEquations = t.stream()
    												.filter(u)
    												.map(translate)
									                .flatMap(AffineModelEquation::from)
									                .map(dimension)
									                .collect(Collectors.toList());

        AffineTransform transform = model.solve(finalEquations,dimension);
        
        return new AffineTransformBuilder(transform, -translate.getX(), -translate.getY()).build();
		        						
	}

}
