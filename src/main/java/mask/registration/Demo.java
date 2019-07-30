package mask.registration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mask.registration.alignment.AlignmentCalculation;
import mask.registration.alignment.AlignmentCorrection;
import mask.registration.alignment.AlignmentTransform;
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
		System.out.println(System.lineSeparator()+ "--- unaligned ------------------" + summary);
		
		
		
		AlignmentCalculation alignmentCalc = new AlignmentCalculation();
		AlignmentTransform alignment = alignmentCalc.apply(displacements, alignmentSelection);
		
		System.out.println(alignment);
		
		AlignmentCorrection alignmentCorrection = new AlignmentCorrection();
		List<Displacement> aligned = alignmentCorrection.apply(alignment, displacements);
		
		DisplacementSummary alignedSummary = Displacement.summarize(aligned, calculationSelection);
		System.out.println(System.lineSeparator()+ "--- aligned --------------------" + alignedSummary);
		
		
	}

	
}
