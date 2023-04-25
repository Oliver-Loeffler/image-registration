/**
 * Apache Math Commons 3 Solver Module for Image-Registration
 */
open module net.raumzeitfalle.registration.mathcommons {
	requires transitive net.raumzeitfalle.registration.solver;
	requires org.apache.commonsmath3;

	exports net.raumzeitfalle.registration.mathcommons;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
}