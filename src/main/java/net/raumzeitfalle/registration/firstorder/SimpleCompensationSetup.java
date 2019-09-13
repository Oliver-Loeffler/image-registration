package net.raumzeitfalle.registration.firstorder;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public class SimpleCompensationSetup implements CompensationSetup {
	
	private final AlignmentSetup alignSetup;
	
	private final Set<Compensations> compensationMethods;
	
	private final Predicate<Displacement> calculationSelection;
	
	SimpleCompensationSetup(AlignmentSetup alignmentSetup, Predicate<Displacement> calculationSelector, Compensations...compensations) {
		this.alignSetup = alignmentSetup;
		this.compensationMethods = collectCompensations(compensations);
		this.calculationSelection = selectForCalculation(alignmentSetup, calculationSelector);
	}

	private Predicate<Displacement> selectForCalculation(AlignmentSetup alignment, Predicate<Displacement> calculationSelector) {
		if (alignment.isOfType(Alignments.SCANNER_SELECTED)) {
			return alignment.getAlignmenSelection();
		}
		return calculationSelector;
	}

	private Set<Compensations> collectCompensations(Compensations[] compensations) {
		Set<Compensations> methods = EnumSet.noneOf(Compensations.class);
		for (Compensations c : compensations)
			methods.add(c);
		return methods;
	}

	@Override
	public Set<Compensations> getCompensations() {
		return this.compensationMethods;
	}

	@Override
	public Predicate<Displacement> getCalculationSelection() {
		return this.calculationSelection;
	}

	@Override
	public FirstOrderSetup selectCalculationSites(Predicate<Displacement> calcSelector) {
		return new FirstOrderSetup(alignSetup, this, d->false);
	}

}
