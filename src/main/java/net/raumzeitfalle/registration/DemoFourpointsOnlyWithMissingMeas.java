/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.DisplacementSummary;
import net.raumzeitfalle.registration.displacement.SiteClass;
import net.raumzeitfalle.registration.displacement.SiteSelection;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.file.FileLoader;
import net.raumzeitfalle.registration.firstorder.Alignments;
import net.raumzeitfalle.registration.firstorder.Compensations;
import net.raumzeitfalle.registration.firstorder.FirstOrderCorrection;
import net.raumzeitfalle.registration.firstorder.FirstOrderResult;
import net.raumzeitfalle.registration.firstorder.FirstOrderSetup;

public class DemoFourpointsOnlyWithMissingMeas {

	public static void main(String ...args) {
		
		DemoFourpointsOnlyWithMissingMeas demo = new DemoFourpointsOnlyWithMissingMeas();
		demo.run();

	}
	
	private void run() {
		
		System.out.println(System.lineSeparator() + "--- DEMO: 4-point alignment, scale & ortho corrected -------");
		System.out.println(System.lineSeparator() + "          with missed measurements ");
		
		// STEP 1, load displacements from file (or any other source)
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point-withNaN.csv"));
		
	
		// STEP 2, perform site selection 
		SiteSelection selection = SiteSelection
								.forAlignment(d -> d.isOfType(SiteClass.ALIGN_MARK))
								.forCalculation(d->true)
								.build()
								.remove(d->d.isOfType(SiteClass.INFO_ONLY));
	
		// STEP 3, parametrize evaluation model 
		FirstOrderSetup setup = FirstOrderSetup
								.usingAlignment(Alignments.SELECTED)
								.withCompensations(Compensations.SCALE, Compensations.ORTHO)
								.withSiteSelection(selection);

	
		// STEP 4, perform correction and calculate results
		FirstOrderResult result = FirstOrderCorrection.using(displacements, setup);
		Collection<Displacement> results = result.getDisplacements();
		
		// STEP 5, print results
		
		// Now print results before correction
		DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator() + "--- unaligned ----------------------------------------------" + uncorrectedSummary);
		
		// after correction
		DisplacementSummary correctedSummary = Displacement.summarize(results, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- corrected ----------------------------------------------" + correctedSummary);
		
		// now also print residual first order and alignment
		System.out.println("--- Residual Alignment and First Order ------------------------------------");
		
		RigidTransform correctedAlignment = result.getAlignment();
		System.out.println(System.lineSeparator() + correctedAlignment);
		
		AffineTransform correctedFirstOrder = result.getFirstOrder();
		System.out.println(System.lineSeparator() + correctedFirstOrder);
		
	}

	
}
