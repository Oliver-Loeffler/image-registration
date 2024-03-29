/**
 * NIST JAMA Solver Module for Image-Registration
 * based on <code>gov.nist.math:jama:1.0.3</code> which is available at Maven Central.
 */
open module net.raumzeitfalle.registration.jama {
	requires jama;
	
	requires transitive net.raumzeitfalle.registration.solver;
	exports net.raumzeitfalle.registration.jama;
	
	provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
	    with net.raumzeitfalle.registration.jama.JamaSolver;
}