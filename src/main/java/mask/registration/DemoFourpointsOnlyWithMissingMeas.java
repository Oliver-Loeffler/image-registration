package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import mask.registration.displacement.Displacement;
import mask.registration.displacement.DisplacementSummary;
import mask.registration.displacement.SiteClass;
import mask.registration.displacement.SiteSelection;
import mask.registration.file.FileLoader;
import mask.registration.firstorder.Alignments;
import mask.registration.firstorder.Compensations;
import mask.registration.firstorder.FirstOrderCorrection;
import mask.registration.firstorder.FirstOrderSetup;

public class DemoFourpointsOnlyWithMissingMeas {

	public static void main(String ...args) {
		
		DemoFourpointsOnlyWithMissingMeas demo = new DemoFourpointsOnlyWithMissingMeas();
		demo.run();

	}

	private void run() {
		System.out.println(System.lineSeparator() + "--- DEMO: Support for incomplete coordinates --");
				
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point-withNaN.csv"));
		
		SiteSelection selection = SiteSelection
				.forAlignment(d -> d.isOfType(SiteClass.ALIGN_MARK))
				.forCalculation(d->true)
				.build()
				.remove(d->d.isOfType(SiteClass.INFO_ONLY));

		FirstOrderCorrection correction = new FirstOrderCorrection();
		FirstOrderSetup setup = new FirstOrderSetup().withAlignment(Alignments.SELECTED)
				.withCompensations(Compensations.SCALE, Compensations.ORTHO).withSiteSelection(selection);

		Collection<Displacement> results = correction.apply(displacements, setup);

		DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator() + "--- unaligned ---------------------------------" + uncorrectedSummary);

		DisplacementSummary correctedSummary = Displacement.summarize(results, selection.getCalculation());
		System.out.println(System.lineSeparator() + "--- 4-point aligned, scale&ortho corrected ----" + correctedSummary);

	}

	
}
