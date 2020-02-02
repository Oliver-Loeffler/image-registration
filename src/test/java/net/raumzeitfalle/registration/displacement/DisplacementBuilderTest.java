package net.raumzeitfalle.registration.displacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DisplacementBuilderTest {

	private Displacement.Builder classUnderTest = Displacement.builder();
	
	@Test
	void builder() {
		assertNotNull(classUnderTest);
	}
	
	@Test
	void noIndexAssigned() {
		
		Throwable t = assertThrows(NullPointerException.class,
				()->classUnderTest.build());
		
		assertEquals("Index must not be null. Displacement Builder is not properly initialized.",
				t.getMessage());
		
	}
	
	@Test
	void indexOnly() {
		
		Throwable t = assertThrows(NullPointerException.class,
				()->classUnderTest.withIndex(42).build());
		
		assertEquals("x must not be null.",
				t.getMessage());
		
	}
	
	@Test
	void minimalBuilderSetup_withAbsoluteDisplacements() {
		
		Displacement d = classUnderTest.withIndex(42)
			      .atLocation(17.0, 11.0)
			      .xDisplacedTo(17.1)
			      .yDisplacedTo(17.0)
			      .withId(9)
				  .build();
		
		assertNotNull(d);
		assertEquals(17.0, d.getX(), 1E-3);
		assertEquals(11.0, d.getY(), 1E-3);
		
		assertEquals(17.1, d.getXd(), 1E-3);
		assertEquals(17.0, d.getYd(), 1E-3);
		
		assertEquals(9, d.getId());
		assertEquals(Category.REG, d.getCategory());
	}
	
	@Test
	void minimalBuilderSetup_withPairwiseAbsoluteDisplacements() {
		
		Displacement d = classUnderTest.withIndex(42)
			      .atLocation(17.0, 11.0)
			      .displacedTo(17.1, 17.0)
			      .withId(9)
				  .build();
		
		assertNotNull(d);
		assertEquals(17.0, d.getX(), 1E-3);
		assertEquals(11.0, d.getY(), 1E-3);
		
		assertEquals(17.1, d.getXd(), 1E-3);
		assertEquals(17.0, d.getYd(), 1E-3);
		
		assertEquals(9, d.getId());
		assertEquals(Category.REG, d.getCategory());
	}
	
	@Test
	void minimalBuilderSetup_withDifferences() {
		
		Displacement d = classUnderTest.withIndex(42)
			      .atLocation(17.0, 11.0)
			      .withXDeviation(0.1)
			      .withYDeviation(6.0)
			      .ofCategory(Category.INFO_ONLY)
				  .get();
		
		assertNotNull(d);
		assertEquals(17.0, d.getX(), 1E-3);
		assertEquals(11.0, d.getY(), 1E-3);
		
		assertEquals(17.1, d.getXd(), 1E-3);
		assertEquals(17.0, d.getYd(), 1E-3);
		
		assertEquals(42, d.getId());
		
		assertEquals(Category.INFO_ONLY, d.getCategory());
	}
}
