package net.raumzeitfalle.registration.solver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DeltasTest {

	@Test
	void test() {
		
		double[] diffs = {1,2,0.2};
		
		Deltas deltas = ()->diffs;
		
		assertAll(
				()->assertEquals(3, deltas.rows(), "row count"),
				()->assertArrayEquals(diffs, deltas.getArray(), "differences array must be equal")
				);
		
	}

}
