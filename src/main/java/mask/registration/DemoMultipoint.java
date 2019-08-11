package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.distortions.AffineTransform;
import mask.registration.distortions.FirstOrderTransformCalculation;
import mask.registration.distortions.FirstOrderTransformCorrection;
import mask.registration.file.FileLoader;

public class DemoMultipoint {

	public static void main(String ...args) {
		
		DemoMultipoint demo = new DemoMultipoint();
		demo.run();

	}

	private void run() {
		
		// define multipoint alignment (aka. DEVA "ALL")
		SiteSelection selection = SiteSelection.multipoint();
			
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo.csv"));
		DisplacementSummary summary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- unaligned --------------------------------" + summary);
		
		RigidTransformCalculation alignmentCalc = new RigidTransformCalculation();
		RigidTransform alignment = alignmentCalc.apply(displacements, selection.getAlignment());
		
		System.out.println(alignment);
		
		Collection<Displacement> aligned = alignment.apply(displacements);
		
		DisplacementSummary alignedSummary = Displacement.summarize(aligned, selection.getCalculation());
		
		System.out.println(System.lineSeparator()+ "--- aligned ----------------------------------" + alignedSummary);
		
		FirstOrderTransformCalculation firstOrderCalc = new FirstOrderTransformCalculation();
		AffineTransform firstOrder = firstOrderCalc.apply(displacements, selection.getFirstOrderSelection());
		
		System.out.println(firstOrder);
		
		FirstOrderTransformCorrection correction = new FirstOrderTransformCorrection(alignment, firstOrder);
		Collection<Displacement> corrected = correction.apply(displacements, selection);
		
		DisplacementSummary correctedSummary = Displacement.summarize(corrected, selection.getFirstOrderSelection());
		System.out.println(System.lineSeparator()+ "--- scaling and shearing removed -------------" + correctedSummary);
		
		AffineTransform finalFirstOrder = firstOrderCalc.apply(corrected, selection.getCalculation());
		System.out.println(finalFirstOrder);
		
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
