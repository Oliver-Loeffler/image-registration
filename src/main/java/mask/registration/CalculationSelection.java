package mask.registration;

import java.util.Objects;
import java.util.function.Predicate;

public class CalculationSelection {
	
	private final Predicate<Displacement> alignment;
	
	private final Predicate<Displacement> calculation;
	
	CalculationSelection(AlignmentSelection alignmentSelection, Predicate<Displacement> selection) {
		Objects.requireNonNull(alignmentSelection, "AlignmentSelection must not be null");
		Objects.requireNonNull(selection, "Predicate for calculation selection must not be null");
		this.alignment = alignmentSelection.get();
		this.calculation = selection;
	}
	
	public SiteSelection build() {
		return new SiteSelection(alignment, calculation, calculation);
	}

	public SiteSelection forFirstOrderCalculation(Predicate<Displacement> firstOrderSelection) {
		return new SiteSelection(alignment, calculation, firstOrderSelection);
	}

}
