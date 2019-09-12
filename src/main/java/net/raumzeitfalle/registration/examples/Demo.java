package net.raumzeitfalle.registration.examples;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.alignment.RigidTransformCalculation;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.DisplacementSummary;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.AffineTransformCalculation;
import net.raumzeitfalle.registration.file.FileLoader;
import net.raumzeitfalle.registration.firstorder.Alignments;
import net.raumzeitfalle.registration.firstorder.Compensations;
import net.raumzeitfalle.registration.firstorder.FirstOrderCorrection;
import net.raumzeitfalle.registration.firstorder.FirstOrderResult;
import net.raumzeitfalle.registration.firstorder.FirstOrderSetup;

public class Demo implements Runnable {
	
	private final Path source;
	
	private Predicate<Displacement> alignmentSelector = d->true;
	
	private Predicate<Displacement> calculationSelector = d->true;
	
	private Predicate<Displacement> removalSelector = d->false;
	
	private Alignments alignments = Alignments.UNALIGNED;
	
	private Compensations[] compensations = new Compensations[0];
	
	private final String title;
	
	public Demo(String title, String filename) {
		this.source = Paths.get(filename);
		this.title = title;
	}
	
	public Demo withAlignment(Alignments alignments) {
		this.alignments = alignments;
		return this;
	}
	
	public Demo withCompensations(Compensations ...compensations) {
		this.compensations = compensations;
		return this;
	}
	
	public Demo selectForAlignment(Predicate<Displacement> alignmentSelector) {
		this.alignmentSelector = alignmentSelector;
		return this;
	}
	
	public Demo selectForCalculation(Predicate<Displacement> calculationSelector) {
		this.calculationSelector = calculationSelector;
		return this;
	}
	
	public Demo selectForRemoval(Predicate<Displacement> removalSelector) {
		this.removalSelector = removalSelector;
		return this;
	}
	
	@Override
	public void run() {
		section(title);
		
		// STEP 1
		List<Displacement> displacements = new FileLoader().load(source);
		
		// STEP 2 
		FirstOrderSetup setup = FirstOrderSetup
						.usingAlignment(alignments)
						.withCompensations(compensations)
						.selectForAlignment(alignmentSelector)
						.selectForCalculation(calculationSelector)
						.removeDisplacments(removalSelector);
		
		// STEP 3
		FirstOrderResult result = FirstOrderCorrection.using(displacements, setup);
		Collection<Displacement> results = result.getDisplacements();

		// STEP 4 
		DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, setup.withoutRemovedDisplacements());
		section("unaligned - before correction (raw evaluation)");
		print(uncorrectedSummary);
		
		RigidTransform uncorrectedAlignment = new RigidTransformCalculation().apply(displacements, setup.withoutRemovedDisplacements());
		print(uncorrectedAlignment);
		
		AffineTransform uncorrectedFirstOrder = new AffineTransformCalculation().apply(displacements, setup.withoutRemovedDisplacements());
		print(uncorrectedFirstOrder);
		
		DisplacementSummary correctedSummary = Displacement.summarize(results, setup.withoutRemovedDisplacements());
		section("aligned and corrected (evaluation using setup)");
		print(correctedSummary);
		
		RigidTransform correctedAlignment = result.getAlignment();
		print(correctedAlignment);
		
		AffineTransform correctedFirstOrder = result.getFirstOrder();
		print(correctedFirstOrder);	
		
		
	}

	private void print(Object object) {
		System.out.println(System.lineSeparator() + object);
	}
	
	private void section(String name) {
		section(name, 100);
	}
	
	private void section(String name, int width) {

		String prefix = "--- ";
		StringBuilder builder = new StringBuilder(System.lineSeparator()).append(prefix).append(name).append(" ");

		int remaining = width - name.length() - 1;

		while (remaining > 0) {
			builder.append("-");
			remaining--;
		}

		System.out.println(builder.toString());
	}

}
