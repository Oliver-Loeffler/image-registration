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
package net.raumzeitfalle.registration.solvertest;

import java.util.*;
import java.util.function.*;

import net.raumzeitfalle.registration.alignment.*;
import net.raumzeitfalle.registration.displacement.*;
import net.raumzeitfalle.registration.distortions.*;
import net.raumzeitfalle.registration.firstorder.*;

public class NumericsTestRunner implements Consumer<List<Displacement>> {
	
	private Predicate<Displacement> alignmentSelector = d->true;
	
	private Predicate<Displacement> calculationSelector = d->true;
	
	private Predicate<Displacement> removalSelector = d->false;
	
	private Alignments alignments = Alignments.UNALIGNED;
	
	private Compensations[] compensations = new Compensations[0];

	private List<Displacement> results;

	private FirstOrderResult result;

	private DisplacementSummary uncorrectedSummary = null;

	private RigidTransform uncorrectedAlignment = null;

	private AffineTransform uncorrectedFirstOrder = null;

	private DisplacementSummary correctedSummary = null;

	private RigidTransform correctedAlignment = null;

	public List<Displacement> getCorrectedResults() {
		return results;
	}

	public FirstOrderResult getFirstOrderResult() {
		return result;
	}

	public DisplacementSummary getUncorrectedSummary() {
		return uncorrectedSummary;
	}

	public RigidTransform getUncorrectedAlignment() {
		return uncorrectedAlignment;
	}

	public AffineTransform getUncorrectedFirstOrder() {
		return uncorrectedFirstOrder;
	}
	
	public AffineTransform getFirstOrder() {
		return firstOrder;
	}

	public DisplacementSummary getCorrectedSummary() {
		return correctedSummary;
	}

	public RigidTransform getCorrectedAlignment() {
		return correctedAlignment;
	}

	public AffineTransform getCorrectedFirstOrder() {
		return correctedFirstOrder;
	}

	private AffineTransform correctedFirstOrder = null;

	private AffineTransform firstOrder = null;
	
	public NumericsTestRunner withAlignment(Alignments alignments) {
		this.alignments = alignments;
		return this;
	}
	
	public NumericsTestRunner withCompensations(Compensations ...compensations) {
		this.compensations = compensations;
		return this;
	}
	
	public NumericsTestRunner selectForAlignment(Predicate<Displacement> alignmentSelector) {
		this.alignmentSelector = alignmentSelector;
		return this;
	}
	
	public NumericsTestRunner selectForCalculation(Predicate<Displacement> calculationSelector) {
		this.calculationSelector = calculationSelector;
		return this;
	}
	
	public NumericsTestRunner selectForRemoval(Predicate<Displacement> removalSelector) {
		this.removalSelector = removalSelector;
		return this;
	}
	
	@Override
	public void accept(List<Displacement> displacements) {
		
		// STEP 2
		FirstOrderSetup setup = FirstOrderSetup.usingAlignment(alignments)
					   .selectForAlignment(alignmentSelector)
					   .compensate(compensations)
					   .selectForCalculation(calculationSelector)
					   .removeDisplacements(removalSelector)
					   .build();
					   
		
		// STEP 3
		this.result = FirstOrderCorrection.using(displacements, setup);
		this.results = new ArrayList<>(result.getDisplacements());

		// STEP 4 
		this.uncorrectedSummary = Displacement.summarize(displacements, setup.withoutRemovedDisplacements());
		this.uncorrectedAlignment = new RigidTransformCalculation().apply(displacements, setup.withoutRemovedDisplacements());
		this.uncorrectedFirstOrder = new AffineTransformCalculation().apply(displacements, setup.withoutRemovedDisplacements());
		
		this.correctedSummary = Displacement.summarize(this.results, setup.withoutRemovedDisplacements());
		this.correctedAlignment = result.getAlignment();
		this.firstOrder  = result.getFirstOrder();
		this.correctedFirstOrder = new AffineTransformCalculation().apply(results, setup.withoutRemovedDisplacements());
	}
	
	public void run(List<Displacement> source) {
		this.accept(source);
	}

}
