module La4jSolver {
    requires transitive SolverAPI;
    requires org.la4j;
    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter 
        with net.raumzeitfalle.registration.la4j.La4jSolver;
    exports net.raumzeitfalle.registration.la4j;
}