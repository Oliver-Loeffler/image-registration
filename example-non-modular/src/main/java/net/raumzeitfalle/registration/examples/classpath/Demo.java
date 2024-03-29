/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.examples.classpath;

import java.io.Console;
import java.io.PrintWriter;
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
		
	private final PrintWriter out;
	
	public Demo(String title, String filename) {
		this.source = Paths.get(filename);
		this.title = title;
		this.out = getWriter();
		// Not needed with "image-registration" artifact, this always defaults to JAMA.
		// SolverProvider.setPreferredImplementation(JamaSolver.class.getName());
	}
	
	private PrintWriter getWriter() {
		
		Console console = System.console();
		if (null != console) {
			return console.writer();
		}
		return new PrintWriter(System.out);
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
		FirstOrderSetup setup = FirstOrderSetup.usingAlignment(alignments)
					   .selectForAlignment(alignmentSelector)
					   .compensate(compensations)
					   .selectForCalculation(calculationSelector)
					   .removeDisplacements(removalSelector)
					   .build();
					   
		
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
		this.out.println(System.lineSeparator() + object);
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

		this.out.println(builder.toString());
	}

}
