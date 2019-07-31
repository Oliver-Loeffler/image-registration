package mask.registration;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import mask.registration.alignment.AlignmentCalculation;
import mask.registration.alignment.AlignmentCorrection;
import mask.registration.alignment.AlignmentTransform;
import mask.registration.file.FileLoader;
import mask.registration.firstorder.FirstOrderCalculation;
import mask.registration.firstorder.FirstOrderTransform;

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
		System.out.println(System.lineSeparator()+ "--- unaligned ------------------" + summary);
		
		
		
		AlignmentCalculation alignmentCalc = new AlignmentCalculation();
		AlignmentTransform alignment = alignmentCalc.apply(displacements, alignmentSelection);
		
		System.out.println(alignment);
		
		AlignmentCorrection alignmentCorrection = new AlignmentCorrection();
		List<Displacement> aligned = alignmentCorrection.apply(alignment, displacements);
		
		DisplacementSummary alignedSummary = Displacement.summarize(aligned, calculationSelection);
		System.out.println(System.lineSeparator()+ "--- aligned --------------------" + alignedSummary);
		
		FirstOrderCalculation firstOrderCalc = new FirstOrderCalculation();
		FirstOrderTransform firstOrder = firstOrderCalc.apply(displacements, calculationSelection);
		
		System.out.println(firstOrder);
		
		
	}

	
}
