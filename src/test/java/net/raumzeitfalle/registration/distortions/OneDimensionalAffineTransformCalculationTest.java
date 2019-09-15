package net.raumzeitfalle.registration.distortions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class OneDimensionalAffineTransformCalculationTest {
	
	private static final double TOLERANCE = 1E-11;
	
	private final BiFunction<Collection<Displacement>, 
							  Predicate<Displacement>, 
							          AffineTransform> funtionUnderTest = new AffineTransformCalculation();


	@Test
	void displacementsAlongVerticalLine() {
		// 1ppm rotation

		double tx = 0.1;
		double ty = -.01;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0, 70000,  20000, 70000+.06+tx,  20000-.06+ty));
		undisplaced.add(Displacement.at(1, 1, 70000,  40000, 70000+.04+tx,  40000-.04+ty));
		undisplaced.add(Displacement.at(2, 2, 70000,  60000, 70000+.02+tx,  60000-.02+ty));
		undisplaced.add(Displacement.at(3, 3, 70000,  80000, 70000    +tx,  80000    +ty));
		undisplaced.add(Displacement.at(4, 4, 70000, 100000, 70000-.02+tx, 100000+.02+ty));
		undisplaced.add(Displacement.at(5, 5, 70000, 120000, 70000-.04+tx, 120000+.04+ty));
		undisplaced.add(Displacement.at(6, 6, 70000, 140000, 70000-.06+tx, 140000+.06+ty));
		
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
		assertEquals( tx, result.getTranslationX(),  TOLERANCE);
		assertEquals( ty, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 70000.0, result.getCenterX(),  TOLERANCE);
		assertEquals( 80000.0, result.getCenterY(),  TOLERANCE);
		
		assertEquals(   0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(   1.000, result.getScaleY()*1E6,        1E-6);
		assertEquals(   0.5  , result.getMagnification()*1E6, 1E-6);
		
		assertEquals(   0.000, result.getOrthoX()*1E6,        TOLERANCE);
		assertEquals(  -1.000, result.getOrthoY()*1E6,        1E-6);
		assertEquals(  -1.000, result.getOrtho()*1E6,         1E-6);
	}

	@Test
	void displacementsAlongHorizontalLine() {
		// 1ppm rotation

		double tx = 0.1;
		double ty = -.01;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,  20000, 70000,  20000-.06+tx, 70000-.06+ty));
		undisplaced.add(Displacement.at(1, 1,  40000, 70000,  40000-.04+tx, 70000-.04+ty));
		undisplaced.add(Displacement.at(2, 2,  60000, 70000,  60000-.02+tx, 70000-.02+ty));
		undisplaced.add(Displacement.at(3, 3,  80000, 70000,  80000+.00+tx, 70000+.00+ty));
		undisplaced.add(Displacement.at(4, 4, 100000, 70000, 100000+.02+tx, 70000+.02+ty));
		undisplaced.add(Displacement.at(5, 5, 120000, 70000, 120000+.04+tx, 70000+.04+ty));
		undisplaced.add(Displacement.at(6, 6, 140000, 70000, 140000+.06+tx, 70000+.06+ty));
			
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
		assertEquals(  tx, result.getTranslationX(),  TOLERANCE);
		assertEquals(  ty, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 80000.0, result.getCenterX(),    TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),    TOLERANCE);
		
		assertEquals(  1.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(  0.0,   result.getScaleY()*1E6,        1E-6);
		assertEquals(  0.500, result.getMagnification()*1E6, 1E-6);
		
		assertEquals( -1.000, result.getOrthoX()*1E6,        1E-6);
		assertEquals(  0.000, result.getOrthoY()*1E6,        TOLERANCE);
		assertEquals(  1.000, result.getOrtho()*1E6,         1E-6);
	}
}