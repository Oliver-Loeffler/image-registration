open module net.raumzeitfalle.registration.core {
	requires java.logging;
	requires transitive net.raumzeitfalle.registration.solver;
	
	exports net.raumzeitfalle.registration;
	exports net.raumzeitfalle.registration.alignment;
	exports net.raumzeitfalle.registration.displacement;
	exports net.raumzeitfalle.registration.distortions;
	exports net.raumzeitfalle.registration.firstorder;
}