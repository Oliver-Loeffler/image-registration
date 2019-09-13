package net.raumzeitfalle.registration.firstorder;

import java.util.Set;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.displacement.Displacement;

public interface CompensationSetup {
	
	FirstOrderSetup selectCalculationSites(Predicate<Displacement> calcSelector);
	
	Set<Compensations> getCompensations();
	
	Predicate<Displacement> getCalculationSelection();
	
}
