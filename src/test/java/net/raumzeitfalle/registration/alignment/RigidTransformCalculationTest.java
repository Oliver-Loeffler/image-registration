package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class RigidTransformCalculationTest {
	
	private static final double TOLERANCE = 1E-11;
	
	private final BiFunction<Collection<Displacement>, 
							  Predicate<Displacement>, 
							          RigidTransform> funtionUnderTest = new RigidTransformCalculation();

	@Test
	void zeroTransform() {
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1000, 1000));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1000, 9000));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9000, 9000));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9000, 1000));
		undisplaced.add(Displacement.at(4, 4, 9000, 1000, Double.NaN, Double.NaN));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals( 0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(), TOLERANCE);
		assertEquals( 0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	@Test
	void translationX() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1010, 1000));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1010, 9000));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9010, 9000));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9010, 1000));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals( 10.0, result.getTranslationX(), TOLERANCE);
		assertEquals(  0.0, result.getTranslationY(), TOLERANCE);
		assertEquals(  0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	@Test
	void translationY() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1000,  990));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1000, 8990));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9000, 8990));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9000,  990));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(   0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( -10.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	@Test
	void translationXY() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1010,  990));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1010, 8990));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9010, 8990));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9010,  990));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(  10.0, result.getTranslationX(), TOLERANCE);
		assertEquals( -10.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	@Test
	void rotation() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		
		double rotated = Math.sqrt(2)*1E3;
		
		undisplaced.add(Displacement.at(0, 0, -1000, -1000,        0,  -rotated));
		undisplaced.add(Displacement.at(0, 0, -1000,  1000, -rotated,         0));
		undisplaced.add(Displacement.at(0, 0,  1000,  1000,        0,   rotated));
		undisplaced.add(Displacement.at(0, 0,  1000, -1000,  rotated,         0));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(   0.0, result.getTranslationX(), TOLERANCE);
		assertEquals(   0.0, result.getTranslationY(), TOLERANCE);
		
		
		/*
		 * As linear approximation is only valid for small angles,
		 * linear rotation will not yield with 45 degree after 
		 * rad to degree conversion.
		 */
		assertEquals(  0.7071067811865474, result.getRotation()    , TOLERANCE);
		assertEquals( 40.514, 180 * result.getRotation() / Math.PI , 1E-3);
	}
	
	@Test
	void rotationAndTranslation() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		
		double rotated = Math.sqrt(2)*1E3;
		
		undisplaced.add(Displacement.at(0, 0, -1000, -1000,        0+10,  -rotated-4));
		undisplaced.add(Displacement.at(0, 0, -1000,  1000, -rotated+10,         0-4));
		undisplaced.add(Displacement.at(0, 0,  1000,  1000,        0+10,   rotated-4));
		undisplaced.add(Displacement.at(0, 0,  1000, -1000,  rotated+10,         0-4));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(  10.0, result.getTranslationX(), TOLERANCE);
		assertEquals(  -4.0, result.getTranslationY(), TOLERANCE);
		
		
		/*
		 * As linear approximation is only valid for small angles,
		 * linear rotation will not yield with 45 degree after 
		 * rad to degree conversion.
		 */
		assertEquals(  0.7071067811865474, result.getRotation()    , TOLERANCE);
		assertEquals( 40.514, 180 * result.getRotation() / Math.PI , 1E-3);
	}
	
	@Test
	void skipTransform() {
		
		List<Displacement> displacements = new ArrayList<>(4);
		displacements.add(Displacement.at(0, 0, 1000, 1000, 1010,  990));
		displacements.add(Displacement.at(1, 1, 1000, 9000, 1010, 8990));
		displacements.add(Displacement.at(2, 2, 9000, 9000, 9010, 8990));
		displacements.add(Displacement.at(3, 3, 9000, 1000, 9010,  990));
	
		Predicate<Displacement> selectNone = d -> false; // no displacement will be used
		
		RigidTransform result = funtionUnderTest.apply(displacements,selectNone);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SkipRigidTransform.class));
	
		assertEquals( 0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(), TOLERANCE);
		assertEquals( 0.0, result.getRotation(),     TOLERANCE);
	
	}
	
	@Test
	void translationXonly1D() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1010, Double.NaN));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1010, Double.NaN));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9010, Double.NaN));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9010, Double.NaN));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(  10.0, result.getTranslationX(), TOLERANCE);
		assertEquals(   0.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	@Test
	void translationYonly1D() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, Double.NaN, 1000));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, Double.NaN, 9100));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, Double.NaN, 9100));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, Double.NaN, 1000));
		
		RigidTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertTrue(result.getClass().equals(SimpleRigidTransform.class));
		
		assertEquals(  0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( 50.0, result.getTranslationY(), TOLERANCE);
		assertEquals(  0.0, result.getRotation(),     TOLERANCE);
		
	}

}
