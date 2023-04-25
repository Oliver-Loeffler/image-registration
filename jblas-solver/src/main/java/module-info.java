/**
 * JBLAS Solver Module for Image Registration
 */
open module net.raumzeitfalle.registration.jblas {
	requires jblas;
	
	requires transitive net.raumzeitfalle.registration.solver;
	exports net.raumzeitfalle.registration.jblas;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.jblas.JblasSolver;
}