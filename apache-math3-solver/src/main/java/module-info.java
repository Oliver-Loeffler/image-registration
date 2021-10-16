module ApacheMathSolver {
    requires SolverAPI;
    requires commons.math3;
    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter 
        with net.raumzeitfalle.registration.mathcommons.ApacheMathCommonsSolver;
}