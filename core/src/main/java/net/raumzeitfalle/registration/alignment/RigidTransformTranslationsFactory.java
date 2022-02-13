/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
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

import java.util.Objects;

import net.raumzeitfalle.registration.DifferencesVector;
import net.raumzeitfalle.registration.OrientedOperation;

class RigidTransformTranslationsFactory implements OrientedOperation<RigidTransform> {
	
	private final DifferencesVector solution;
	
	public RigidTransformTranslationsFactory(DifferencesVector differences) {
		this.solution = Objects.requireNonNull(differences, "differences must never be null");
	}

	@Override
	public RigidTransform getX() {
		return RigidTransform.shiftX(solution.get(0));
	}

	@Override
	public RigidTransform getY() {
		return RigidTransform.shiftY(solution.get(0));
	}

	@Override
	public RigidTransform getCombined() {
		return RigidTransform.translation(solution.get(0), solution.get(1));
	}

}
