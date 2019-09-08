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

/**
 * Combines the selection criteria for data alignment and calculation.<br>
 * Usually each calculation is prepared by an alignment step. This class
 * provides the required predicates to select displacements for alignment and
 * for subsequent calculation.
 * <br>
 * {@link CalculationSelection} instances are immutable.
 * 
 * @author Oliver Loeffler
 *
 */
public class CalculationSelection {

	private final AlignmentSelection alignment;

	private final Predicate<Displacement> calculation;

	/**
	 * Creates a new {@link CalculationSelection} from the given {@link AlignmentSelection} and a {@link Predicate} to select sites for calculation.
	 * @param alignmentSelection
	 * @param positionalCalculationSelection
	 */
	CalculationSelection(AlignmentSelection alignmentSelection,
			Predicate<Displacement> positionalCalculationSelection) {
		Objects.requireNonNull(alignmentSelection, "AlignmentSelection must not be null");
		Objects.requireNonNull(positionalCalculationSelection, "Predicate for calculation selection must not be null");
		this.alignment = alignmentSelection;
		this.calculation = positionalCalculationSelection;
	}
	
	/**
	 * Builds a new {@link SiteSelection} instance, using the given {@link AlignmentSelection} and this {@link CalculationSelection} while removing later on all {@link Displacement} instances of the given {@link Category}.
	 * {@link SiteSelection} instances are immutable.
	 * 
	 * @param categoryToRemove {@link Category} All displacements of the given category will be removed.
	 * 
	 * @return {@link SiteSelection}
	 */
	public SiteSelection remove(Category categoryToRemove) {
		return new SiteSelection(alignment.get(), calculation, d->d.belongsTo(categoryToRemove));
	}
	
	/**
	 * Builds a new {@link SiteSelection} instance, using the given {@link AlignmentSelection} and this {@link CalculationSelection}.
	 * {@link SiteSelection} instances are immutable.
	 * 
	 * @param removalSelector {@link Predicate} to match {@link Displacement} instances which shall be removed.
	 * 
	 * @return {@link SiteSelection}
	 */
	public SiteSelection remove(Predicate<Displacement> toBeRemoved) {
		return new SiteSelection(alignment.get(), calculation, toBeRemoved);
	}

	/**
	 * Builds a new {@link SiteSelection} instances, whereas {@link SiteSelection} allows it to futher specify {@link Predicate} instances to select {@link Displacement} instances for removal.
	 * {@link SiteSelection} instances are immutable.
	 * 
	 * @return {@link SiteSelection}
	 */
	public SiteSelection build() {
		return new SiteSelection(alignment.get(), calculation);
	}

}
