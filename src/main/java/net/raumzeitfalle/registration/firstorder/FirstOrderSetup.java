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

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public final class FirstOrderSetup {
		
	private Alignments alignment;
	
	private Set<Compensations> compensations;
	
	private Predicate<Displacement> calculationSelection;
	
	private Predicate<Displacement> alignmentSelection;
	
	private Predicate<Displacement> removalSelection;
	
	public static FirstOrderSetup usingAlignment(Alignments alignment) {
		return new FirstOrderSetup().withAlignment(alignment);
	}
	
	public FirstOrderSetup() {
		this.alignmentSelection = d->true;   // use all displacements for alignment calculation
		this.calculationSelection = d->true; // calculate distortions on all displacements
		this.removalSelection = d->false;    // no displacements to be removed 
		this.alignment = Alignments.UNALIGNED;
		this.compensations = EnumSet.noneOf(Compensations.class);
	}
	
	public FirstOrderSetup withAlignment(Alignments newAlignment) {
		this.alignment = newAlignment;
		return this;
	}
	
	public FirstOrderSetup withCompensations(Compensations ...compensation) {
		for (Compensations c : compensation) {
			this.compensations.add(c);
		}
		return this;
	}
	
	public FirstOrderSetup selectForAlignment(Predicate<Displacement> selection) {
		Objects.requireNonNull(selection, "Predicate for alignment selection must not be null.");
		this.alignmentSelection = selection;
		return this;
	}
	
	public FirstOrderSetup selectForCalculation(Predicate<Displacement> selection) {
		Objects.requireNonNull(selection, "Predicate for calculation selection must not be null.");
		this.calculationSelection = selection;
		return this;
	}
	
	public FirstOrderSetup removeDisplacments(Predicate<Displacement> selection) {
		Objects.requireNonNull(selection, "Predicate for displacement removal must not be null.");
		this.removalSelection = selection;
		return this;
	}

	public Alignments getAlignment() {
		return alignment;
	}

	public Set<Compensations> getCompensations() {
		return compensations;
	}

	

	@Override
	public String toString() {
		return String.format(
				"FirstOrderSetup [alignment=%s, compensations=%s, calculationSelection=%s, alignmentSelection=%s, removalSelection=%s]",
				alignment, compensations, calculationSelection, alignmentSelection, removalSelection);
	}

	public Predicate<Displacement> getAlignmenSelection() {
		if (alignment.equals(Alignments.ALL))
			return d->true; // select all sites
			
		if (alignment.equals(Alignments.UNALIGNED))
			return d->true; // alignment is not performed but calculated 

		return this.alignmentSelection;
	}

	public Predicate<Displacement> getCalculationSelection() {
		if (alignment.equals(Alignments.SCANNER_SELECTED)) {
			return this.alignmentSelection;
		}
		return this.calculationSelection;
	}

	public Predicate<Displacement> withoutRemovedDisplacements() {
		return this.removalSelection.negate();
	}
	
	
}
