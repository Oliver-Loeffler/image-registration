package net.raumzeitfalle.registration.distortions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

class SkipAffineTransformTest {

	private final Transform classUnderTest = new SkipAffineTransform(0d, 0d);

	@Test
	void skip() {
		assertTrue(classUnderTest.skip());
	}
	
	@Test
	void centeredAt() {
		assertTrue(SkipAffineTransform.centeredAt(0, 0).skip());
	}
	
	@Test
	void apply() {
		Displacement source = Displacement.at(0, 1, 17.2, -2.7);
		Displacement result = classUnderTest.apply(source);
		
		assertEquals(source, result);
		assertEquals(0, result.getIndex());
		assertEquals(1, result.getId());
		
		assertEquals(17.2, result.getX(), 1E-3);
		assertEquals(-2.7, result.getY(), 1E-3);
		
		assertEquals(17.2, result.getXd(), 1E-3);
		assertEquals(-2.7, result.getYd(), 1E-3);
	}
	
	@Test
	void getCenter() {
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getCenterX(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getCenterY(), 1E-11);
	}
	
	@Test
	void getTranslation() {
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void getScale() {
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getScaleX(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getScaleY(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getMagnification(), 1E-11);
	}
	
	@Test
	void getOrtho() {
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getOrthoX(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getOrthoY(), 1E-11);
		assertEquals(0.0, ((SkipAffineTransform) classUnderTest).getOrtho(), 1E-11);
	}
}
