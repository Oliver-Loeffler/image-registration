package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DimensionTest {
	
	private Dimension classUnderTest;

	@Test
	void accept() {
		
		classUnderTest = new Dimension();
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Direction.UNKNOWN, classUnderTest.getDirection());
		
		classUnderTest.accept(Direction.X);
		
		assertEquals(1, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Direction.X, classUnderTest.getDirection());
		
		classUnderTest.accept(Direction.Y);
		classUnderTest.accept(Direction.X);
		
		assertEquals(2, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Direction.XY, classUnderTest.getDirection());
		
		assertEquals("Dimension [XY (x=2,y=1)]", classUnderTest.toString());
		
		classUnderTest = new Dimension();
		classUnderTest.accept(Direction.Y);
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Direction.Y, classUnderTest.getDirection());
		
		
	}

}
