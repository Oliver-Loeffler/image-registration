/**
 * JAMA: A Java Matrix Package
 * (also usable as a solver for image registration)
 */
open module net.raumzeitfalle.jama {
    requires transitive net.raumzeitfalle.registration.solver;
    exports net.raumzeitfalle.jama;

    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter
            with net.raumzeitfalle.jama.Solver;
}