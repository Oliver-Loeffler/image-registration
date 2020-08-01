package net.raumzeitfalle.registration.solver;

import net.raumzeitfalle.registration.jama.JamaSolver;

public class SolverProvider {
	
	public Solver getSolver() {
	    // return new EjmlSolver();
	    // return new La4jSolver();
		return new JamaSolver();
		// return new ApacheMathCommonsSolver();
	}
	
}
