open module net.raumzeitfalle.registration.jama {
	requires jama;
	
	requires transitive net.raumzeitfalle.registration.solver;
	exports net.raumzeitfalle.registration.jama;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.jama.JamaSolver;
}