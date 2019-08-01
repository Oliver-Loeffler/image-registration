package mask.registration;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import mask.registration.alignment.RigidCorrection;
import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.distortions.AffineTransform;
import mask.registration.distortions.FirstOrderTransformCalculation;
import mask.registration.distortions.FirstOrderTransformCorrection;
import mask.registration.file.FileLoader;

public class Demo {

	public static void main(String ...args) {
		
		Demo demo = new Demo();
		demo.run();

	}

	private void run() {
		
		Predicate<Displacement> alignmentSelection = d -> true; // MULTIPOINT;
		Predicate<Displacement> calculationSelection = d -> true; // use all locations for summary;
		
		
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo.csv"));
		DisplacementSummary summary = Displacement.summarize(displacements, calculationSelection);
		System.out.println(System.lineSeparator()+ "--- unaligned --------------------------------" + summary);
		
		
		
		RigidTransformCalculation alignmentCalc = new RigidTransformCalculation();
		RigidTransform alignment = alignmentCalc.apply(displacements, alignmentSelection);
		
		System.out.println(alignment);
		
		RigidCorrection alignmentCorrection = new RigidCorrection();
		List<Displacement> aligned = alignmentCorrection.apply(alignment, displacements);
		
		DisplacementSummary alignedSummary = Displacement.summarize(aligned, calculationSelection);
		
		System.out.println(System.lineSeparator()+ "--- aligned ----------------------------------" + alignedSummary);
		
		FirstOrderTransformCalculation firstOrderCalc = new FirstOrderTransformCalculation();
		AffineTransform firstOrder = firstOrderCalc.apply(displacements, calculationSelection);
		
		System.out.println(firstOrder);
		
		FirstOrderTransformCorrection correction = new FirstOrderTransformCorrection(alignment, firstOrder);
		List<Displacement> corrected = correction.apply(displacements);
		
		DisplacementSummary correctedSummary = Displacement.summarize(corrected, calculationSelection);
		System.out.println(System.lineSeparator()+ "--- scaling and shearing removed -------------" + correctedSummary);
		
		
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
