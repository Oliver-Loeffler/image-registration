package mask.registration.displacement;

import java.util.Objects;
import java.util.function.Predicate;

public class CalculationSelection {
	
	private final AlignmentSelection alignment;
	
	private final Predicate<Displacement> calculation;
	
	CalculationSelection(AlignmentSelection alignmentSelection, Predicate<Displacement> positionalCalculationSelection) {
		Objects.requireNonNull(alignmentSelection, "AlignmentSelection must not be null");
		Objects.requireNonNull(positionalCalculationSelection, "Predicate for calculation selection must not be null");
		this.alignment = alignmentSelection;
		this.calculation = positionalCalculationSelection;
	}
	
	public SiteSelection build() {
		return new SiteSelection(alignment.get(), calculation, calculation);
	}

	public SiteSelection forFirstOrderCalculation(Predicate<Displacement> firstOrderSelection) {
		return new SiteSelection(alignment.get(), calculation, firstOrderSelection);
	}
	
}
