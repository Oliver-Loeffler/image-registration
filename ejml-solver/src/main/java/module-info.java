/**
 * EJML Solver Module for Image-Registration
 */
open module net.raumzeitfalle.registration.ejml {
	requires ejml.simple.bundle;
	requires transitive net.raumzeitfalle.registration.solver;
	exports net.raumzeitfalle.registration.ejml;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.ejml.EjmlSolver;
}