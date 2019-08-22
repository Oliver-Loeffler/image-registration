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
package net.raumzeitfalle.registration.displacement;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AlignmentSelection implements Supplier<Predicate<Displacement>> {
	
	private final Predicate<Displacement> selection;

	AlignmentSelection(Predicate<Displacement> selection) {
		this.selection = Objects.requireNonNull(selection, "Predicate for alignment selection must not be null.");
	}
	
	public CalculationSelection forCalculation(Predicate<Displacement> selection) {
		return new CalculationSelection(this, selection);
	}

	@Override
	public Predicate<Displacement> get() {
		return this.selection;
	}
}
