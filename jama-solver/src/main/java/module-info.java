module JamaSolver {
    requires Jama;
    requires transitive SolverAPI;
    
    provides net.raumzeitfalle.registration.solver.spi.SolverAdapter 
        with net.raumzeitfalle.registration.jama.JamaSolver;
    
    exports net.raumzeitfalle.registration.jama;
}