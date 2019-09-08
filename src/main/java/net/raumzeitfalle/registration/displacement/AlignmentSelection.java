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

/**
 * Controls how displacements are selected for data alignment.
 * <p>
 * {@link AlignmentSelection} instances are immutable.
 * 
 * @author Oliver Loeffler
 *
 */
public class AlignmentSelection implements Supplier<Predicate<Displacement>> {
	
	private final Predicate<Displacement> selection;

	/**
	 * Creates a new {@link AlignmentSelection} with the given {@link Predicate}
	 * 
	 * @param selection Selection criterion for data alignment
	 */
	AlignmentSelection(Predicate<Displacement> selection) {
		this.selection = Objects.requireNonNull(selection, "Predicate for alignment selection must not be null.");
	}
	
	/**
	 * Uses the given {@link Predicate} and creates a {@link CalculationSelection} instances based from this {@link AlignmentSelection}.
	 * @param selection for calculation (positional and first order)
	 * @return {@link CalculationSelection}
	 */
	public CalculationSelection forCalculation(Predicate<Displacement> selection) {
		return new CalculationSelection(this, selection);
	}

	/**
	 * Provides the {@link Predicate} used to select displacements for alignment.
	 * 
	 * @return {@link Predicate}
	 */
	@Override
	public Predicate<Displacement> get() {
		return this.selection;
	}
}
