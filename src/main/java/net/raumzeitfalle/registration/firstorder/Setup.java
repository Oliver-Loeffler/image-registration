package net.raumzeitfalle.registration.firstorder;

import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public interface Setup {

	Alignments getAlignment();
	
	Predicate<Displacement> getAlignmenSelection();
	
}
