package net.raumzeitfalle.registration.solver;

import java.util.*;

import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

public class SolverProvider {
	
	public static void setPreferredImplementation(String className) {
		if ("".equalsIgnoreCase(className)) {
			throw new IllegalArgumentException("className must not be empty.");
		}
		
		if (!Objects.equals(className, preferredSolverClass)) {
			getInstance().forceRediscovery();
		}
		
		preferredSolverClass = className;
	}

	private static String preferredSolverClass = null;

	private SolverAdapter preferredImplementation = null;
	
	public static synchronized SolverProvider getInstance() {
        if (service == null) {
            service = new SolverProvider();
        }
        return service;
    }
	
	private static SolverProvider service;
	
	private ServiceLoader<SolverAdapter> loader;
	
	private SolverProvider() {
        loader = ServiceLoader.load(SolverAdapter.class);
    }
	
	protected List<SolverAdapter> getAllAvailableImplementations() {
		List<SolverAdapter> discoveredImplementations = new ArrayList<>();
		Iterator<SolverAdapter> availableImplementations = loader.iterator();
		while(availableImplementations.hasNext()) {
			SolverAdapter solver = availableImplementations.next();
			discoveredImplementations.add(solver);
		}
		return discoveredImplementations;
    }
	
	public SolverAdapter getSolver() {
		if (this.preferredImplementation != null) {
			return this.preferredImplementation;
		}
		
		List<SolverAdapter> solver = getAllAvailableImplementations();
		if (preferredSolverClass != null) {
			Optional<SolverAdapter> option = solver.stream()
										    .filter(s->s.getClass().getName().equals(preferredSolverClass))
										    .findAny();
			
			if (option.isPresent()) {
				
				SolverAdapter newPreference = option.get();
				setPreferredSolver(newPreference);
				return newPreference;
			}
			
			throw new IllegalArgumentException(String.format("There is no solver with class [%s] configured.", preferredSolverClass));				  
		}
		
		SolverAdapter lastFound = getLastImplementation(solver); 
		setPreferredSolver(lastFound);
		return this.preferredImplementation;
	}

	private void setPreferredSolver(SolverAdapter newPreference) {
		this.preferredImplementation = newPreference;
	}
	
	private void forceRediscovery() {
		this.preferredImplementation = null;
	}

	private SolverAdapter getLastImplementation(List<SolverAdapter> solver) {
		if (solver.isEmpty()) {
			throw new IllegalArgumentException("There is no solver implementation configured.");
		}
		return solver.get(solver.size()-1);
	}

}
