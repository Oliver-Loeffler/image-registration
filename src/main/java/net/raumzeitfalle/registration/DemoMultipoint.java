package net.raumzeitfalle.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.DisplacementSummary;
import net.raumzeitfalle.registration.displacement.SiteSelection;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.file.FileLoader;
import net.raumzeitfalle.registration.firstorder.Alignments;
import net.raumzeitfalle.registration.firstorder.FirstOrderCorrection;
import net.raumzeitfalle.registration.firstorder.FirstOrderResult;
import net.raumzeitfalle.registration.firstorder.FirstOrderSetup;

public class DemoMultipoint {

	public static void main(String ...args) {
		
		DemoMultipoint demo = new DemoMultipoint();
		demo.run();

	}

	private void run() {

		System.out.println(System.lineSeparator() + "--- DEMO: Multipoint alignment	-----------------------------");
		
		// STEP 1, load displacements from file (or any other source)
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point.csv"));
		
	
		// STEP 2, perform site selection 
		SiteSelection selection = SiteSelection
								.forAlignment(d -> true)
								.forCalculation(d->true)
								.build();
	
		// STEP 3, parametrize evaluation model 
		FirstOrderSetup setup = FirstOrderSetup
								.usingAlignment(Alignments.ALL)
								.withSiteSelection(selection);

		// STEP 4, perform correction and calculate results
		FirstOrderResult result = new FirstOrderCorrection().apply(displacements, setup);
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
