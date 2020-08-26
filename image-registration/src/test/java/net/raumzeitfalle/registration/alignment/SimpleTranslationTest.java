package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class SimpleTranslationTest {

	private Translation classUnderTest = null;
	

	@Test
	void skip() {
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertFalse(classUnderTest.skip());
	}
		
	@Test
	void translation() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertEquals( 10.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-20.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}
	
	@Test
	void shiftx() {
		
		classUnderTest = SimpleTranslation.shiftX(10.0);
		
		assertEquals( 10.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(  0.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}
	
	@Test
	void shifty() {
		
		classUnderTest = SimpleTranslation.shiftY(-22.0);
		
		assertEquals(  0.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-22.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void apply() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		double x = 20;
		double y = 30;
		
		Displacement source = Displacement.at(0, 0, x, y);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( 10, result.getXd());
		assertEquals( 50, result.getYd());
	}
	
	@Test
	void toStringMethod() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertEquals("Translation [x=10.0000000, y=-20.0000000]", classUnderTest.toString());
		
	}

}
