package net.raumzeitfalle.registration.distortions;

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

class AffineTransformCalculationTest {

private static final double TOLERANCE = 1E-11;
	
	private final BiFunction<Collection<Displacement>, 
							  Predicate<Displacement>, 
							          AffineTransform> funtionUnderTest = new AffineTransformCalculation();

	@Test
	void zeroTransform() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0,    0,    0,    0,    0));
		undisplaced.add(Displacement.at(1, 1,    0, 8000,    0, 8000));
		undisplaced.add(Displacement.at(2, 2, 9000, 8000, 9000, 8000));
		undisplaced.add(Displacement.at(3, 3, 9000,    0, 9000,    0));
		undisplaced.add(Displacement.at(4, 4, 4500, 4000, Double.NaN, Double.NaN));
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(AffineTransform.class));
		
		assertEquals( 0.0, result.getTranslationX(),  TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 4500.0, result.getCenterX(),    TOLERANCE);
		assertEquals( 4000.0, result.getCenterY(),    TOLERANCE);
		
		assertEquals( 0.0, result.getScaleX(),        TOLERANCE);
		assertEquals( 0.0, result.getScaleY(),        TOLERANCE);
		assertEquals( 0.0, result.getMagnification(), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),        TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),        TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),         TOLERANCE);
	}
	
	@Test
	void positiveScaling() {
		
		double dx = 0.075; // 150nm over 150um is 1ppm
		double dy = 0.070; // 140nm over 140um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, displacement->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(AffineTransform.class));
		
		assertEquals( 0.0, result.getTranslationX(),  TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),    TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),    TOLERANCE);
		
		assertEquals( 1E-6, result.getScaleX(),        TOLERANCE);
		assertEquals( 1E-6, result.getScaleY(),        TOLERANCE);
		assertEquals( 1E-6, result.getMagnification(), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),        TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),        TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),         TOLERANCE);
	}

}
