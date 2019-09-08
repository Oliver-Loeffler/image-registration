package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

class SimpleRigidTransformTest {

	private final Transform classUnderTest = SimpleRigidTransform.with(10, -20, 3);

	@Test
	void skip() {
		assertFalse(classUnderTest.skip());
	}
	
	@Test
	void rotation() {
		assertEquals( 3.0, ((RigidTransform) classUnderTest).getRotation(), 1E-11);
	}
	
	@Test
	void translation() {
		assertEquals( 10.0, ((RigidTransform) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-20.0, ((RigidTransform) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void apply() {
		Displacement source = Displacement.at(0, 0, 10, 20);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( 60, result.getXd());
		assertEquals( 10, result.getYd());
	}

}
