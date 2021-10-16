module EjmlSolver {
    requires transitive SolverAPI;   
    requires net.raumzeitfalle.ejml.simple.bundle;
    
    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter 
        with net.raumzeitfalle.registration.ejml.EjmlSolver;
    
    exports net.raumzeitfalle.registration.ejml;
}