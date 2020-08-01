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
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.Dimension;
import net.raumzeitfalle.registration.displacement.Displacement;

/**
 * 
 * Calculates rotation and translation (x,y) for the given collection of displacements.
 * A rigid body transform will preserve all distances between points.
 *  
 * @author oliver
 *
 */
public final class RigidTransformCalculation implements BiFunction<Collection<Displacement>, Predicate<Displacement>, RigidTransform>{
	
	private final RigidBodyModel model;
	
	public RigidTransformCalculation() {
		this(new DefaultRigidBodyModel());
	}

	public RigidTransformCalculation(RigidBodyModel model) {
		this.model = Objects.requireNonNull(model, "The used model for calculation must not be null (RigidBodyModel).");
	}

	/**
	 * Calculates the alignment parameters (translation<sub>X</sub>, translation<sub>Y</sub> and rotation) for the given collection of displacements.
	 * When no displacements are left after filtering with the given predicate, then the {@link SkipRigidTransform} is returned. Otherwise the results is returned as {@link SimpleRigidTransform}.
	 * <p>
	 * The {@link DefaultRigidBodyModel} is used for calculation.
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
						
		Dimension<RigidModelEquation> dimension = new Dimension<>();
		
		List<RigidModelEquation> equations = t.stream()
											 .filter(u)
											 .flatMap(RigidModelEquation::from)
											 .map(dimension)
											 .collect(Collectors.toList());
		
		if (equations.isEmpty()) {
			return continueUnaligned();
		}
		
		return model.solve(equations, dimension);
	}

	private RigidTransform continueUnaligned() {
		return new SkipRigidTransform();
	}
	
}
