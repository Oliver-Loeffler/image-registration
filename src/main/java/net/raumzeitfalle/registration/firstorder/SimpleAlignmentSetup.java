package net.raumzeitfalle.registration.firstorder;

import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public class SimpleAlignmentSetup implements AlignmentSetup {
	
	private Alignments alignment;
	
	private Predicate<Displacement> alignmentSelection;
	
	SimpleAlignmentSetup(Alignments alignmentMethod, Predicate<Displacement> alignmentSelector) {
		this.alignment = alignmentMethod;
		this.alignmentSelection = verifyAndUpdate(alignmentSelector);
	}

	private Predicate<Displacement> verifyAndUpdate(Predicate<Displacement> alignmentSelector) {
		if (alignment.equals(Alignments.ALL))
			return d->true; // select all sites
			
		if (alignment.equals(Alignments.UNALIGNED))
			return d->true; // alignment is not performed but calculated 

		return this.alignmentSelection;
	} 

	@Override
	public Alignments getAlignment() {
		return this.alignment;
	}

	@Override
	public Predicate<Displacement> getAlignmenSelection() {
		return this.alignmentSelection;
	}

	@Override
	public boolean isOfType(Alignments other) {
		return this.alignment.equals(other);
	}

	@Override
	public AlignmentSetup selectForAlignment(Predicate<Displacement> selector) {
		return new SimpleAlignmentSetup(alignment, selector);
	}

	@Override
	public CompensationSetup withCompensations(Compensations... compensations) {
		return new SimpleCompensationSetup(this, d->true, compensations);
	}
}
