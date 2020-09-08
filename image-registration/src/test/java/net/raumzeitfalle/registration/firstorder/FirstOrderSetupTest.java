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
package net.raumzeitfalle.registration.firstorder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import net.raumzeitfalle.registration.displacement.Displacement;


class FirstOrderSetupTest {
	
	private FirstOrderSetup classUnderTest;

	private static final Predicate<Displacement> ANY = d -> true;
	
	private static final Predicate<Displacement> FIRST_SITE = d -> d.getIndex() == 1;

	private static final Predicate<Displacement> THIRD_SITE = d -> d.getIndex() == 3;
	
	@Test
	void testDefaults() {

		classUnderTest = FirstOrderSetup.builder().build();

		List<Displacement> source = getDisplacements(3);
		
		List<Displacement> alignment = filter(source, classUnderTest.getAlignmentSelection());
		List<Displacement> calculation = filter(source, classUnderTest.getCalculationSelection());
		List<Displacement> removed = filter(source, classUnderTest.getRemovalSelection());
		
		assertAll(
				() -> assertEquals(source.size(), alignment.size(), "# of alignment sites matches # of source sites"),
				() -> assertEquals(source.size(), calculation.size(), "# of calculation sites matches # of source sites"),
				() -> assertEquals(0, removed.size(), "By default no sites are removed."),
				() -> assertEquals(Alignments.UNALIGNED, classUnderTest.getAlignment()),
				() -> assertTrue(classUnderTest.getCompensations().isEmpty()));

		Set<Compensations> compensations = classUnderTest.getCompensations();

		assertTrue(compensations.isEmpty());

		assertThrows(UnsupportedOperationException.class, () -> compensations.add(Compensations.MAGNIFICATION));
	}
	
	@Test
	void runWithDefaults() {
		
		List<Executable> assertions = new ArrayList<>();

		classUnderTest = FirstOrderSetup.builder().build();

		List<Displacement> displacements = new ArrayList<>();
		displacements.add(Displacement.at(1, 1, 0d, 0d));
		displacements.add(Displacement.at(2, 2, 0d, 0d));
		displacements.add(Displacement.at(3, 3, 0d, 0d));

		Predicate<Displacement> align = classUnderTest.getAlignmentSelection();
		
		List<Displacement> forAlignment = displacements.stream()
													   .filter(align)
													   .collect(Collectors.toList());
		
		assertions.add(()->assertEquals(displacements.size(), forAlignment.size()
				, "all sites selected for alignment"));
		
		
		
		Predicate<Displacement> calc = classUnderTest.getAlignmentSelection();
		List<Displacement> forCalculation = displacements.stream()
				   .filter(calc)
				   .collect(Collectors.toList());

		assertions.add(()->assertEquals(displacements.size(), forCalculation.size()
				,"all sites for calculation"));
		
		Predicate<Displacement> remove = classUnderTest.getRemovalSelection();
		List<Displacement> forRemoval = displacements.stream()
				   .filter(remove)
				   .collect(Collectors.toList());

		assertions.add(()->assertEquals(0, forRemoval.size()
				,"no sites to be removed"));
		
		Predicate<Displacement> withoutRemovedLocations = classUnderTest.withoutRemovedDisplacements();		
		List<Displacement> withoutRemoved = displacements.stream()
				   .filter(withoutRemovedLocations)
				   .collect(Collectors.toList());

		assertions.add(()->assertEquals(displacements.size(), withoutRemoved.size(),"Number of sites should match number of source sites."));
		
		assertAll(assertions);
	}

	@Test
	void compensationsAreUnmodifiable() {

		// Plain builder without customization
		Executable defaultCase = () -> {
			Set<Compensations> compensations = FirstOrderSetup.builder().build().getCompensations();

			assertThrows(UnsupportedOperationException.class, () -> compensations.add(Compensations.MAGNIFICATION));
		};

		// Builder with custom compensations added
		Executable customCase = () -> {
			Set<Compensations> compensations = FirstOrderSetup.builder()
					.compensate(Compensations.MAGNIFICATION, Compensations.ORTHO).build().getCompensations();

			assertThrows(UnsupportedOperationException.class, () -> compensations.add(Compensations.MAGNIFICATION));
		};

		assertAll(defaultCase, customCase);

	}

	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "UNALIGNED", "ALL" })
	void standardAlignmentPredicates(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
				              .selectForAlignment(FIRST_SITE)
				              .build();

		
		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());
		List<Displacement> forCalculation = filter(source, classUnderTest.getCalculationSelection());
		
		assertEquals(source.size(), forAlignment.size());
		assertEquals(source.size(), forCalculation.size());
	}
	
	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "UNALIGNED", "ALL" })
	void standardAlignmentPredicates_withRemovedSite(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
				              .selectForAlignment(FIRST_SITE)
				              .removeDisplacements(THIRD_SITE)
				              .build();

		
		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());
		List<Displacement> forCalculation = filter(source, classUnderTest.getCalculationSelection());
		List<Displacement> forRemoval = filter(source, classUnderTest.getRemovalSelection());
		
		assertEquals(source.size() - forRemoval.size(), forAlignment.size());
		assertEquals(source.size() - forRemoval.size(), forCalculation.size());
	}


	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "SCANNER_SELECTED", "SELECTED" })
	void selectedAlignmentPredicates(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
				                        .selectForAlignment(FIRST_SITE)
				                        .build();

		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());
		List<Displacement> forRemoval = filter(source, classUnderTest.getRemovalSelection());
		
		assertEquals(1, forAlignment.size());
		assertEquals(0, forRemoval.size());
		
	}

	@ParameterizedTest
	@CsvSource({ "SCALE, 'SCALE, ORTHO', 'MAGNIFICATION, ORTHO', ORTHO" })
	void configureCompensations(String source) {
		String[] sources = source.split(",");
		Set<Compensations> expected = EnumSet.noneOf(Compensations.class);

		FirstOrderSetup.Builder builder = FirstOrderSetup.builder();
		for (String s : sources) {
			Compensations comp = Compensations.valueOf(s);
			builder.compensate(comp);
			expected.add(comp);
		}

		classUnderTest = builder.build();

		List<Executable> asserts = new ArrayList<>();

		asserts.add(() -> assertEquals(sources.length, classUnderTest.getCompensations().size(),
				"number of compensation methods"));
		for (Compensations method : expected) {
			asserts.add(
					() -> assertTrue(classUnderTest.getCompensations().contains(method), "requires " + method.name()));
		}

		assertAll(asserts);
	}

	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "ALL", "UNALIGNED" })
	void selectedForCalculationPredicates_standard(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
				.selectForAlignment(FIRST_SITE)
				.selectForCalculation(ANY)
				.build();
		
		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());

		assertEquals(source.size(), forAlignment.size());

	} 

	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "SELECTED" })
	void selectedForCalculationPredicates(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
							.selectForAlignment(FIRST_SITE)
							.selectForCalculation(ANY)
							.build();

		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());

		assertEquals(1, forAlignment.size());

	}
	
	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "SCANNER_SELECTED" })
	void selectedForCalculationPredicates_scanner(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment)
				.selectForAlignment(FIRST_SITE)
				.selectForCalculation(ANY)
				.build();

		List<Displacement> source = getDisplacements(3);
		List<Displacement> forAlignment = filter(source, classUnderTest.getAlignmentSelection());
		List<Displacement> forCalculation = filter(source, classUnderTest.getCalculationSelection());
		
		assertEquals(1, forAlignment.size());
		assertEquals(1, forCalculation.size());
	}

	@Test
	void removalOfDisplacement() {

		classUnderTest = FirstOrderSetup.builder().removeDisplacements(FIRST_SITE).build();

		List<Displacement> displacements = new ArrayList<>();
		displacements.add(Displacement.at(1, 1, 0d, 0d));
		displacements.add(Displacement.at(2, 2, 0d, 0d));
		displacements.add(Displacement.at(3, 3, 0d, 0d));

		Predicate<Displacement> filter = classUnderTest.withoutRemovedDisplacements();
		List<Displacement> filtered = displacements.stream().filter(filter).collect(Collectors.toList());

		Predicate<Displacement> removalCriterion = classUnderTest.getRemovalSelection();
		List<Displacement> sitesMatchingRemovalPred = filtered.stream().filter(removalCriterion)
				.collect(Collectors.toList());

		
		assertAll(
			() -> assertEquals(displacements.size() - 1, filtered.size(), "number of sites after filtering"),
			() -> assertTrue(sitesMatchingRemovalPred.isEmpty(),
						"No sites left which match the removal criterion."));
	}
	
	private List<Displacement> filter(List<Displacement> displacements, Predicate<Displacement> filter) {
		return displacements.stream()
					        .filter(filter)
					        .collect(Collectors.toList());
	}
	
	
	private List<Displacement> getDisplacements(int count) {
		
		Random xr = new Random();
		Random yr = new Random();
		List<Displacement> displacements = new ArrayList<>(count);
		for (int d = 1; d < count+1; d++) {
			double x = xr.nextDouble()*1E5;
			double y = yr.nextDouble()*1E5;
			Displacement displacement = Displacement.at(d, d, x, y);
			displacements.add(displacement);	
		}
		return displacements;
	}
}
