package net.raumzeitfalle.registration.solver;

public class SolverProvider {
	
	public Solver getSolver() {
	    // return new EjmlSolver();
	    // return new La4jSolver();
		return null; //return new JamaSolver();
		// return new ApacheMathCommonsSolver();
	}
	
}
