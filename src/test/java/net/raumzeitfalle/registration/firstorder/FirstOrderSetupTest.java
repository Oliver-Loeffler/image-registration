package net.raumzeitfalle.registration.firstorder;

import static net.raumzeitfalle.registration.firstorder.FirstOrderSetup.ANY;
import static net.raumzeitfalle.registration.firstorder.FirstOrderSetup.NONE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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

	private static final Predicate<Displacement> FIRST_SITE = d -> d.getIndex() == 1;

	@Test
	void testDefaults() {

		classUnderTest = FirstOrderSetup.builder().build();

		assertAll(
				() -> assertEquals(ANY, classUnderTest.getAlignmenSelection()),
				() -> assertEquals(ANY, classUnderTest.getCalculationSelection()),
				() -> assertEquals(NONE, classUnderTest.getRemovalSelection()),
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

		Predicate<Displacement> align = classUnderTest.getAlignmenSelection();
		
		List<Displacement> forAlignment = displacements.stream()
													   .filter(align)
													   .collect(Collectors.toList());
		
		assertions.add(()->assertEquals(displacements.size(), forAlignment.size()
				, "all sites selected for alignment"));
		
		
		
		Predicate<Displacement> calc = classUnderTest.getAlignmenSelection();
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

		classUnderTest = FirstOrderSetup.usingAlignment(alignment).selectForAlignment(FIRST_SITE).build();

		assertNotEquals(FIRST_SITE, classUnderTest.getAlignmenSelection());

		assertEquals(ANY, classUnderTest.getAlignmenSelection());

	}

	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "SCANNER_SELECTED", "SELECTED" })
	void selectedAlignmentPredicates(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment).selectForAlignment(FIRST_SITE).build();

		assertEquals(FIRST_SITE, classUnderTest.getAlignmenSelection());

		assertNotEquals(ANY, classUnderTest.getAlignmenSelection());

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
	@EnumSource(value = Alignments.class, names = { "ALL", "UNALIGNED", "SELECTED" })
	void selectedForCalculationPredicates_standard(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment).selectForAlignment(FIRST_SITE)
				.selectForCalculation(ANY).build();

		assertEquals(ANY, classUnderTest.getCalculationSelection());

		assertNotEquals(FIRST_SITE, classUnderTest.getCalculationSelection());

	}

	@ParameterizedTest
	@EnumSource(value = Alignments.class, names = { "SCANNER_SELECTED" })
	void selectedForCalculationPredicates_scanner(Alignments alignment) {

		classUnderTest = FirstOrderSetup.usingAlignment(alignment).selectForAlignment(FIRST_SITE)
				.selectForCalculation(ANY).build();

		assertEquals(FIRST_SITE, classUnderTest.getCalculationSelection());

		assertNotEquals(ANY, classUnderTest.getCalculationSelection());

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
}
