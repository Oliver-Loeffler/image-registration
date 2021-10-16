module Core {
    requires java.logging;
    requires SolverAPI;
    
    uses net.raumzeitfalle.registration.solver.spi.SolverAdapter;
    
    exports net.raumzeitfalle.registration;
    exports net.raumzeitfalle.registration.displacement;
    exports net.raumzeitfalle.registration.alignment;
    exports net.raumzeitfalle.registration.distortions;
    exports net.raumzeitfalle.registration.firstorder;
}