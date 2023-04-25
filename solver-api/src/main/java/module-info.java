/**
 * API/SPI module to be used when implementing custom Image Registration Solver.
 */
open module net.raumzeitfalle.registration.solver {
	exports net.raumzeitfalle.registration.solver;
	exports net.raumzeitfalle.registration.solver.spi;
	uses net.raumzeitfalle.registration.solver.spi.SolverAdapter;
}