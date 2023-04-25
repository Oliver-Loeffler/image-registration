/**
 * LA4J (linear algebra 4 java) Solver Module for Image Registration
 */
open module net.raumzeitfalle.registration.la4j {
	requires transitive net.raumzeitfalle.registration.solver;
	requires la4j;

	exports net.raumzeitfalle.registration.la4j;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.la4j.La4jSolver;
}