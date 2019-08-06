package mask.registration;

import java.util.function.Predicate;

public class SiteSelection {
	
	public static SiteSelection multipoint() {
		Predicate<Displacement> all = d->true;
		return SiteSelection
					.forAlignment(all)
					.forCalculation(all)
					.forFirstOrderCalculation(all);
	}

	public static AlignmentSelection forAlignment(Predicate<Displacement> alignment) {
		return new AlignmentSelection(alignment);
	}
	
	public static AlignmentSelection alignOn(SiteClass siteClass) {
		return new AlignmentSelection(d->d.isOfType(siteClass));
	}
	
	private final Predicate<Displacement> alignment;
	
	private final Predicate<Displacement> calculation;
	
	private final Predicate<Displacement> firstOrderSelection;

	SiteSelection(Predicate<Displacement> alignment, Predicate<Displacement> calculation,
			Predicate<Displacement> firstOrderSelection) {
		this.alignment = alignment;
		this.calculation = calculation;
		this.firstOrderSelection = firstOrderSelection;
	}
	
	public Predicate<Displacement> getAlignment() {
		return alignment;
	}

	public Predicate<Displacement> getCalculation() {
		return calculation;
	}

	public Predicate<Displacement> getFirstOrderSelection() {
		return firstOrderSelection;
	}
	
	
}
