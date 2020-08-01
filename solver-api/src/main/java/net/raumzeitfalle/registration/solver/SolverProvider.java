package net.raumzeitfalle.registration.solver;

import net.raumzeitfalle.registration.solver.spi.Solver;

public class SolverProvider {
	
	public Solver getSolver() {
	    // return new EjmlSolver();
	    // return new La4jSolver();
		return null; //return new JamaSolver();
		// return new ApacheMathCommonsSolver();
	}
	
}
