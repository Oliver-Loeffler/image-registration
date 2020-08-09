package net.raumzeitfalle.registration.solver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReferencesTest {

	@Test
	void test() {
		
		double[][] design = { 
				{ -75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ -75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, -75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, -75000.0, 0.0, 0.0, 1.0 } };
		
		References references = ()->design;
		
		
		assertAll(
				()->assertEquals(8, references.rows(), "row count"),
				()->assertArrayEquals(design, references.getArray(), "design arrays must be equal")
				);
	}
	


}
