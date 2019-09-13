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
package net.raumzeitfalle.registration.firstorder;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public final class FirstOrderSetup {
		
	private AlignmentSetup alignmentSetup;
	
	private CompensationSetup compensationSetup;

	private Predicate<Displacement> removalSelection;
	
	public static SimpleAlignmentSetup usingAlignment(Alignments alignment) {
		return new SimpleAlignmentSetup(alignment, d->true);
	}
	
	FirstOrderSetup(AlignmentSetup alignment, CompensationSetup compensation, Predicate<Displacement> toBeRemoved) {
		this.alignmentSetup = alignment;
		this.compensationSetup = compensation;
	}
	
	
	public FirstOrderSetup removeDisplacments(Predicate<Displacement> selection) {
		Objects.requireNonNull(selection, "Predicate for displacement removal must not be null.");
		
		return new FirstOrderSetup(this.alignmentSetup, this.compensationSetup, selection);
	}

	public Alignments getAlignment() {
		return this.alignmentSetup.getAlignment();
	}

	public Set<Compensations> getCompensations() {
		return this.compensationSetup.getCompensations();
	}

	public Predicate<Displacement> getAlignmenSelection() {
		return this.alignmentSetup.getAlignmenSelection();
	}

	public Predicate<Displacement> getCalculationSelection() {
		return this.compensationSetup.getCalculationSelection();
	}

	public Predicate<Displacement> withoutRemovedDisplacements() {
		return this.removalSelection.negate();
	}
	
}
