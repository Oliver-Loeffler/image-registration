package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DimensionTest {
	
	private Dimension<Orientable> classUnderTest;

	@Test
	void accept() {
		
		classUnderTest = new Dimension<>();
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.UNKNOWN, classUnderTest.getDirection());
		assertEquals(0, classUnderTest.getDimensions());
		
		classUnderTest.accept(()->Orientation.X);
		
		assertEquals(1, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.X, classUnderTest.getDirection());
		assertEquals(1, classUnderTest.getDimensions());
		
		classUnderTest.accept(()->Orientation.Y);
		classUnderTest.accept(()->Orientation.X);
		
		assertEquals(2, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.XY, classUnderTest.getDirection());
		assertEquals(2, classUnderTest.getDimensions());
		
		assertEquals("Dimension [XY (x=2,y=1)]", classUnderTest.toString());
		
		classUnderTest = new Dimension<>();
		classUnderTest.accept(()->Orientation.Y);
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.Y, classUnderTest.getDirection());
		assertEquals(1, classUnderTest.getDimensions());
		
	}
	
}
