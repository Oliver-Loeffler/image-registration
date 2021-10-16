module JblasSolver {
    requires transitive SolverAPI;
    requires org.jblas;
    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter 
        with net.raumzeitfalle.registration.jblas.JblasSolver;
    
    exports net.raumzeitfalle.registration.jblas;
}