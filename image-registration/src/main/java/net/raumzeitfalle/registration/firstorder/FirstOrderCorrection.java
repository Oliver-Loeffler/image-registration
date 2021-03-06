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

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.TransformCorrection;
import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.alignment.RigidTransformCalculation;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.AffineTransformBuilder;
import net.raumzeitfalle.registration.distortions.AffineTransformCalculation;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;

public final class FirstOrderCorrection implements BiFunction<Collection<Displacement>, FirstOrderSetup, FirstOrderResult> {

	public static FirstOrderResult using(Collection<Displacement> displacements, FirstOrderSetup setup) {
		return new FirstOrderCorrection().apply(displacements, setup);
	}
	
	@Override
	public FirstOrderResult apply(Collection<Displacement> displacements, FirstOrderSetup setup) {
		
		Predicate<Displacement> alignmentSelection = setup.getAlignmentSelection();
		
		/* 
		 * STEP 1 - Calculate corrected first order based on unaligned data
		 */
		RigidTransform alignment = new RigidTransformCalculation()
									   .apply(displacements, alignmentSelection);
		
		Predicate<Displacement> firstOrderSelector = setup.getCalculationSelection();
		
		/*
		 * STEP 2 - Calculate the 6-parameter model (FirstOrder) and parameterize according to corrections defined in setup
		 */
		AffineTransform calculatedFirstOrder = new AffineTransformCalculation().apply(displacements, firstOrderSelector);
		AffineTransform firstOrder = updateFirstOrderForCompensation(setup.getCompensations(),calculatedFirstOrder);
	
		/*
		 * STEP 3 - Apply all requested compensations
		 */
		Collection<Displacement> correctedResults = new TransformCorrection()
				.apply(firstOrder, displacements);
		
		/*
		 * When no alignment is requested, then the work is done here. The result will be returned then.
		 */
		if (setup.getAlignment().equals(Alignments.UNALIGNED)) {
			return new FirstOrderResult(alignment, calculatedFirstOrder, correctedResults);
		}
		
		/*
		 * STEP 4 - When any case of alignment is requested, then a 2nd correction is needed.
		 * Calculate and correct residual rotation correction for custom alignment methods
		 * 
		 */
		RigidTransform residualAlignment = new RigidTransformCalculation().apply(correctedResults, alignmentSelection);
		Collection<Displacement> alignedResults = new TransformCorrection().apply(residualAlignment, correctedResults);
				
		return new FirstOrderResult(alignment, calculatedFirstOrder, alignedResults);
	}

	
	/**
	 * In order to correct a certain transform, this method ensures that depending on given set of compensations,
	 * the proper values are selected and set in a given {@link SimpleAffineTransform}.
	 * 
	 * @param compensations {@link Set}t of {@link Compensations} to apply.
	 * @param transform Calculated {@link AffineTransform}
	 * @return {@link SimpleAffineTransform} prepared on order to perform requested {@link Compensations}
	 */
	private AffineTransform updateFirstOrderForCompensation(Set<Compensations> compensations,
			AffineTransform transform) {
		
		AffineTransformBuilder transformBuilder = new AffineTransformBuilder(transform, 0.0, 0.0);
		
		if (compensations.isEmpty()) {
			transformBuilder.disableAll();
			return transformBuilder.build();
		}
		
		if (!compensations.contains(Compensations.ORTHO)) {
			transformBuilder.disableOrthoXY();
		}
		
		if (compensations.contains(Compensations.MAGNIFICATION)) {
			transformBuilder.useMagnification();
		} else if (!compensations.contains(Compensations.SCALE)) {
			transformBuilder.disableScaleXY();
		}
		
		return transformBuilder.build();
	}

}
