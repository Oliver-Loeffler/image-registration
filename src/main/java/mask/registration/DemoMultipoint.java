package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import mask.registration.displacement.Displacement;
import mask.registration.displacement.DisplacementSummary;
import mask.registration.displacement.SiteSelection;
import mask.registration.file.FileLoader;
import mask.registration.firstorder.Alignments;
import mask.registration.firstorder.FirstOrderCorrection;
import mask.registration.firstorder.FirstOrderSetup;

public class DemoMultipoint {

	public static void main(String ...args) {
		
		DemoMultipoint demo = new DemoMultipoint();
		demo.run();

	}

	private void run() {
		System.out.println(System.lineSeparator() + "--- DEMO: Multi point alignment ---------------");
		
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point.csv"));
		
		SiteSelection selection = SiteSelection
					.forAlignment(d->true)  // FIXME: There is still a unlucky conflict between site selection and alignment mode
					.forCalculation(d->true)
					.build();
	
		FirstOrderCorrection correction = new FirstOrderCorrection();
		FirstOrderSetup setup = new FirstOrderSetup()
										.withAlignment(Alignments.ALL)
										.withSiteSelection(selection);
		
		Collection<Displacement> results = correction.apply(displacements, setup);
		
		DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- unaligned --------------------------------" + uncorrectedSummary);
		
		DisplacementSummary correctedSummary = Displacement.summarize(results, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- multipoint corrected ---------------------" + correctedSummary);
		
	}
}
