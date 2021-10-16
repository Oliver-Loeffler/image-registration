package net.raumzeitfalle.registration.solver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class SolverProviderTest {

	private SolverProvider classUnderTest = SolverProvider.getInstance();

	@BeforeEach
	void prepare() {
		SolverProvider.setPreferredImplementation(null);
	}
	
	@Test
	void getAllAvailableImplementations() {
		
		List<SolverAdapter> implementations = classUnderTest.getAllAvailableImplementations();
		
		assertTrue(implementations.isEmpty(), "no implementations expected to be found");
		
	}
	
	@Test
	void getSolver_missingConfiguration() {
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getSolver());
		
		assertEquals("There is no solver implementation configured.", t.getMessage());
		
	}
	
	@Test
	void useOfCustomSolver_missingCustomSolver() {
		SolverProvider.setPreferredImplementation("myCustomSolverClass");
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getSolver());
		
		assertEquals("There is no solver with class [myCustomSolverClass] configured.", t.getMessage());
	}

}
