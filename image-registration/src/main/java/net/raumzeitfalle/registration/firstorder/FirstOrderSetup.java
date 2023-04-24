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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public final class FirstOrderSetup {
	
	public static FirstOrderSetup.Builder usingAlignment(Alignments alignment) {
		return builder().withAlignment(alignment);
	}
	
	public static FirstOrderSetup.Builder builder() {
		return new Builder();
	}
	
	private static final Predicate<Displacement> ANY = d->true;
	
	private static final Predicate<Displacement> NONE = d->false;
		
	private final Alignments alignmentMethod;
	
	private final Set<Compensations> compensationMethods;
	
	private final Predicate<Displacement> alignmentSelection;
	
	private final Predicate<Displacement> calculationSelection;
	
	private final Predicate<Displacement> removalSelection;
	
	private FirstOrderSetup(Alignments alignMethod,EnumSet<Compensations> compensationMethods,
					Predicate<Displacement> align,Predicate<Displacement> calc,
					Predicate<Displacement> removal) {
		
		this.alignmentMethod = alignMethod;
		this.compensationMethods = Collections.unmodifiableSet(EnumSet.copyOf(compensationMethods));
		
		this.alignmentSelection = align;
		this.calculationSelection = calc;
		this.removalSelection = removal;
		
	}

	public Alignments getAlignment() {
		return this.alignmentMethod;
	}

	public Set<Compensations> getCompensations() {
		return this.compensationMethods;
	}

	public Predicate<Displacement> getAlignmentSelection() {
		return this.alignmentSelection;
	}

	public Predicate<Displacement> getCalculationSelection() {
		return this.calculationSelection;
	}
	
	public Predicate<Displacement> getRemovalSelection() {
		return this.removalSelection;
	}

	public Predicate<Displacement> withoutRemovedDisplacements() {
		return getRemovalSelection().negate();
	}
	
	
	public static class Builder {
		
		/*
		 * default:
		 *  no changes will be made to data as the standard
		 *  alignment method is unaligned.
		 *  
		 *  This means that all parameters like translation, rotation
		 *  are calculated but no correction will be applied.
		 *  
		 */
		private Alignments alignment = Alignments.UNALIGNED;
		
		/*
		 * default:
		 *  
		 *  All first order distortions such as scale and
		 *  non-orthogonality (shear) will be calculated but
		 *  no correction will be applied.
		 *  
		 */
		private EnumSet<Compensations> compensationMethods = EnumSet.noneOf(Compensations.class);
		
		/*
		 * default:
		 *  all displacements will be used for alignment (rigid body model)
		 *  calculation
		 *  
		 */
		private Predicate<Displacement> forAlignment = ANY;
		
		/*
		 * default: 
		 *  all displacements will be used for first-order
		 *  and statistics calculation
		 *  
		 */
		private Predicate<Displacement> forCalculation = ANY;
		
		/*
		 * default:
		 *  no sites will be removed from final result
		 *   
		 */
		private Predicate<Displacement> forRemoval = NONE;

		
		public FirstOrderSetup build() {
			
			Predicate<Displacement> alignmentSelector = getAlignmentSelector();
			Predicate<Displacement> calculationSelector = getCalculationSelector();
			
			return new FirstOrderSetup(alignment, 
					                   compensationMethods,
					                   alignmentSelector, 
					                   calculationSelector,
					                   forRemoval);
		}


		public FirstOrderSetup.Builder withAlignment(Alignments alignment) {
			this.alignment = Objects.requireNonNull(alignment, "The alignment method given must not be null");
			return this;
		}
		
		public FirstOrderSetup.Builder compensate(Compensations ...methods) {
			for (Compensations m : methods) {
				this.compensationMethods.add(m);
			}
			return this;
		}
		
		public FirstOrderSetup.Builder selectForAlignment(Predicate<Displacement> selector) {
			this.forAlignment = Objects.requireNonNull(selector, "Selector for displacements used during alignment must not be null.");
			return this;
		}
		
		public FirstOrderSetup.Builder selectForCalculation(Predicate<Displacement> selector) {
			this.forCalculation = Objects.requireNonNull(selector, "Selector for displacements used for first-order-calculation must not be null.");
			return this;
		}
		
		public FirstOrderSetup.Builder removeDisplacements(Predicate<Displacement> selector) {
			this.forRemoval = Objects.requireNonNull(selector, "Selector for displacements to be removed must not be null.");
			return this;
		}

		private Predicate<Displacement> getCalculationSelector() {
			if (alignment.equals(Alignments.SCANNER_SELECTED)) {
				/* 
				 * This alignment method requires to calculate all first order
				 * details (scale,ortho,translation,rotation) on the alignment marks.
				 *
				 * In case of any corrections, only the amount of distortion 
				 * calculated on selected displacements is corrected.
				 * Thus, there will be a remaining first order distortion,
				 * which technically means that residual values for: translation, 
				 * rotation, scale and ortho will not equal zero.
				 */
				
				return forAlignment;
			}
			
			/*
			 * For all other alignment methods, first order distortions
			 * are calculated on all available displacements (except these
			 * which are matched by the removal predicate).
			 */
			return forCalculation.and(forRemoval.negate());
		}

		private Predicate<Displacement> getAlignmentSelector() {
			if (alignment.equals(Alignments.ALL))
				return ANY.and(forRemoval.negate()); // select all sites, except the ones to be removed or excluded
				
			if (alignment.equals(Alignments.UNALIGNED))
				return ANY.and(forRemoval.negate()); // alignment is not performed but calculated, except the one to be excluded

			return this.forAlignment;
		}
		
	}
}
