package net.raumzeitfalle.registration.solver;

import java.util.*;

import net.raumzeitfalle.registration.solver.spi.Solver;

public class SolverProvider {
	
	public static void setPreferredImplementation(String className) {
		if ("".equalsIgnoreCase(className)) {
			throw new IllegalArgumentException("className must not be empty.");
		}
		preferredImplementation = className;
	}
	
	private static String preferredImplementation = null;

	public static synchronized SolverProvider getInstance() {
        if (service == null) {
            service = new SolverProvider();
        }
        return service;
    }
	
	private static SolverProvider service;
	
	private ServiceLoader<Solver> loader;
	
	private SolverProvider() {
        loader = ServiceLoader.load(Solver.class);
    }
	
	protected List<Solver> getAllAvailableImplementations() {
		List<Solver> discoveredImplementations = new ArrayList<>();
		Iterator<Solver> availableImplementations = loader.iterator();
		while(availableImplementations.hasNext()) {
			Solver solver = availableImplementations.next();
			discoveredImplementations.add(solver);
		}
		return discoveredImplementations;
    }
	
	public Solver getSolver() {
		List<Solver> solver = getAllAvailableImplementations();	
		
		if (preferredImplementation != null) {
			Optional<Solver> option = solver.stream()
										    .filter(s->s.getClass().getName().equals(preferredImplementation))
										    .findAny();
			
			if (option.isPresent()) {
				return option.get();
			}
			
			throw new IllegalArgumentException(String.format("There is no solver with class [%s] configured.", preferredImplementation));				  
		}
		
		return getLastImplementation(solver);
	}

	private Solver getLastImplementation(List<Solver> solver) {
		if (solver.isEmpty()) {
			throw new IllegalArgumentException("There is no solver implementation configured.");
		}
		return solver.get(solver.size()-1);
	}

}