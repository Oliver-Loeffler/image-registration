package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.distortions.AffineTransform;
import mask.registration.distortions.FirstOrderTransformCalculation;
import mask.registration.distortions.FirstOrderTransformCorrection;
import mask.registration.file.FileLoader;

public class DemoFourpointsOnlyWithMissingMeas {

	public static void main(String ...args) {
		
		DemoFourpointsOnlyWithMissingMeas demo = new DemoFourpointsOnlyWithMissingMeas();
		demo.run();

	}

	private void run() {
				
		// define custom sites for alignment and calc
		Predicate<Displacement> isAlignMark = d -> d.isOfType(SiteClass.ALIGN); // MULTIPOINT;
		
		SiteSelection selection = SiteSelection
					.forAlignment(isAlignMark)
					.forCalculation(isAlignMark)
					.forFirstOrderCalculation(isAlignMark);
	
		
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point-withNaN.csv"));
		DisplacementSummary summary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- unaligned --------------------------------" + summary);
		
		RigidTransformCalculation alignmentCalc = new RigidTransformCalculation();
		RigidTransform alignment = alignmentCalc.apply(displacements, selection.getAlignment());
		
		System.out.println(alignment);
		
		Collection<Displacement> aligned = alignment.apply(displacements);;
		
		DisplacementSummary alignedSummary = Displacement.summarize(aligned, selection.getCalculation());
		
		System.out.println(System.lineSeparator()+ "--- aligned ----------------------------------" + alignedSummary);
		
		FirstOrderTransformCalculation firstOrderCalc = new FirstOrderTransformCalculation();
		AffineTransform firstOrder = firstOrderCalc.apply(displacements, selection.getFirstOrderSelection());
		
		System.out.println(firstOrder);
		
		FirstOrderTransformCorrection correction = new FirstOrderTransformCorrection(alignment, firstOrder);
		List<Displacement> corrected = correction.apply(displacements, selection);
		
		DisplacementSummary correctedSummary = Displacement.summarize(corrected, selection.getCalculation());
		
		SiteSelection customized4point = SiteSelection.alignOn(SiteClass.ALIGN)
													  .forCalculation(d->Double.compare(d.getX(), 5425.0) != 0)
													  .build();
		
		
		DisplacementSummary correctedOverallSummary = Displacement.summarize(corrected, customized4point.getCalculation());
		System.out.println(System.lineSeparator()+ "--- scaling and shearing removed -------------" + correctedSummary);
		System.out.println(System.lineSeparator()+ "--- Summary after correction -----------------" + correctedOverallSummary);
		
		AffineTransform firstOrderOverall = firstOrderCalc.apply(displacements, customized4point.getCalculation());
		
		System.out.println(firstOrderOverall);
		
		
		/* TODO: Fix current affine correction and implement straight forward firstOrderCorrection
		 * 	     Create a new transform type FirstOrder which combines Rigid and Affine.
		 * 
		 */
		
		// TODO: Implement sections for positional and first order in DisplacementSummary
		
		// TODO: Add Similarity Transform
		
		/* TODO: Implement strategy to use subset of displacements for alignment
		 *       but all displacement for distortion calculation.
		 */
		
		/* TODO: Implement strategy to use subset of displacements for alignment
		 *       and same subset for distortion calculation.
		 */
	}

	
}
