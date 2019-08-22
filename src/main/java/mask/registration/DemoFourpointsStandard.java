package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import mask.registration.alignment.RigidTransform;
import mask.registration.displacement.Displacement;
import mask.registration.displacement.DisplacementSummary;
import mask.registration.displacement.SiteClass;
import mask.registration.displacement.SiteSelection;
import mask.registration.distortions.AffineTransform;
import mask.registration.file.FileLoader;
import mask.registration.firstorder.Alignments;
import mask.registration.firstorder.Compensations;
import mask.registration.firstorder.FirstOrderCorrection;
import mask.registration.firstorder.FirstOrderResult;
import mask.registration.firstorder.FirstOrderSetup;

public class DemoFourpointsStandard {

	public static void main(String ...args) {
		
		DemoFourpointsStandard demo = new DemoFourpointsStandard();
		demo.run();

	}

	private void run() {
		System.out.println(System.lineSeparator() + "--- DEMO: standard 4-point alignment -----------------------");
		
		// STEP 1, load displacements from file (or any other source)
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point.csv"));
		
	
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
