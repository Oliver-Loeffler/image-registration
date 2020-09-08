package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class OrientationsTest {

	private Orientations classUnderTest = new Orientations();
	
	private Orientation orientation;
	
	@Test
	void x() {
		
		List<Integer> xValues = listOf(1,2,3,4,5);
		List<Integer> yValues = listOf();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.X, orientation),
				()->assertEquals(1, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("X", orientation.toString(), "Name")
				);
	}
	
	@Test
	void y() {
		
		List<Integer> yValues = listOf(1,2,3,4,5);
		List<Integer> xValues = listOf();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.Y, orientation),
				()->assertEquals(1, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("Y", orientation.toString(), "Name")
				);
	}

	@Test
	void empty_xy() {
		
		List<Integer> yValues = Collections.emptyList();
		List<Integer> xValues = Collections.emptyList();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.XY, orientation),
				()->assertEquals(2, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("XY", orientation.toString(), "Name")
				);
	}
	
	@Test
	void populated_xy() {
		
		List<Integer> yValues = listOf(1,2,3,4,5);
		List<Integer> xValues = listOf(4,5);
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.XY, orientation),
				()->assertEquals(2, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("XY", orientation.toString(), "Name")
				);
	}
	
	private <T> List<T> listOf(@SuppressWarnings("unchecked") T ... values) {
		return Arrays.asList(values);
	}

}
