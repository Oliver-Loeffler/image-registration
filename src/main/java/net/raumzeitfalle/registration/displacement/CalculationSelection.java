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

public class CalculationSelection {
	
	private final AlignmentSelection alignment;
	
	private final Predicate<Displacement> calculation;
	
	CalculationSelection(AlignmentSelection alignmentSelection, Predicate<Displacement> positionalCalculationSelection) {
		Objects.requireNonNull(alignmentSelection, "AlignmentSelection must not be null");
		Objects.requireNonNull(positionalCalculationSelection, "Predicate for calculation selection must not be null");
		this.alignment = alignmentSelection;
		this.calculation = positionalCalculationSelection;
	}
	
	public SiteSelection build() {
		return new SiteSelection(alignment.get(), calculation, calculation);
	}

	public SiteSelection forFirstOrderCalculation(Predicate<Displacement> firstOrderSelection) {
		return new SiteSelection(alignment.get(), calculation, firstOrderSelection);
	}
	
}
