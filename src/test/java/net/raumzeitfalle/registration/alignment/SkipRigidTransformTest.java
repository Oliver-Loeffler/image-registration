package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

class SkipRigidTransformTest {
	
	private final Transform classUnderTest = new SkipRigidTransform();

	@Test
	void skip() {
		assertTrue(classUnderTest.skip());
	}
	
	@Test
	void rotation() {
		assertEquals(0.0, ((RigidTransform) classUnderTest).getRotation(), 1E-11);
	}
	
	@Test
	void translation() {
		assertEquals(0.0, ((RigidTransform) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(0.0, ((RigidTransform) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void apply() {
		Displacement source = Displacement.at(0, 0, 10, 20);
		Displacement result = classUnderTest.apply(source);
		
		assertEquals( source, result );
		assertEquals( source.hashCode(), result.hashCode() );
	}
}
