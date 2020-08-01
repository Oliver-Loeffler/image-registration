package net.raumzeitfalle.registration.solvertest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.Solver;

class SolverProviderTest {

	@Test
	void unknownSolverRequested() {
		
		SolverProvider.setPreferredImplementation("thisClassDoesNotExist");
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->SolverProvider.getInstance().getSolver());
		
		
		
		assertEquals("There is no solver with class [thisClassDoesNotExist] configured.", t.getMessage());
		
	}
	
	@Test
	void anySolverRequested() {
		
		SolverProvider.setPreferredImplementation(null);
		
		assertDoesNotThrow(()->{
			
			Solver solver = SolverProvider.getInstance().getSolver();
			
			assertNotNull(solver);
			
			
		});
		
	}

}
