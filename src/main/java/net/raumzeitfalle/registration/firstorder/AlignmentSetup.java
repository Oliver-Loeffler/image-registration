package net.raumzeitfalle.registration.firstorder;

import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public interface AlignmentSetup {

	Alignments getAlignment();
	
	AlignmentSetup selectForAlignment(Predicate<Displacement> selector);
	
	boolean isOfType(Alignments alignment);
	
	Predicate<Displacement> getAlignmenSelection();
	
	CompensationSetup withCompensations(Compensations ...compensations);
	
}
