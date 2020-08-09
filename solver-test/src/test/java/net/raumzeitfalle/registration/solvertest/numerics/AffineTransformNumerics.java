package net.raumzeitfalle.registration.solvertest.numerics;

import static org.junit.jupiter.api.Assertions.*;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.*;
import net.raumzeitfalle.registration.firstorder.*;

/**
 * Holds all common test cases for affine transform calculations AND corrections.
 */
/*
 * TODO: Add assertions for properly performed corrections.
 */
public abstract class AffineTransformNumerics extends RigidTransformNumerics {
	
	private static final double TOLERANCE = 1E-11;

	@Test
	void zeroTransform() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0,    0,    0,    0,    0));
		undisplaced.add(Displacement.at(1, 1,    0, 8000,    0, 8000));
		undisplaced.add(Displacement.at(2, 2, 9000, 8000, 9000, 8000));
		undisplaced.add(Displacement.at(3, 3, 9000,    0, 9000,    0));
		undisplaced.add(Displacement.at(4, 4, 4500, 4000, Double.NaN, Double.NaN));
		
		run(undisplaced);
		
		AffineTransform result = getUncorrectedFirstOrder();
		
		assertNotNull(result);
		
		assertTrue( result.skip() );
		
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
	void scalingX() {
		
		double dx =  0.075; // 150nm over 150um is 1ppm
		double dy =  0.0;   // no impact on y-axis
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.0, result.getTranslationX(),          TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),          TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals(  0.5, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),                TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),                TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),                 TOLERANCE);
	}
	
	@Test
	void scalingY_withoutX() {
		
		double dx =  0.0;   // 150nm over 150um is 1ppm
		double dy = -0.140; // 140nm over 140um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.0, result.getTranslationX(),          TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),          TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals( -2.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals( -1.0, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),                TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),                TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),                 TOLERANCE);
	}
	
	@Test
	void scalingX_withoutY() {
		
		double dx =  0.075; // 150nm over 150um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,  Double.NaN));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx,  Double.NaN));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx,  Double.NaN));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,  Double.NaN));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.0, result.getTranslationX(),          TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),          TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals(  0.5, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),                TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),                TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),                 TOLERANCE);
	}
	
	@Test
	void scalingXY() {
		
		double dx =  0.075; // 150nm over 150um is 1ppm
		double dy = -0.140; // 140nm over 140um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.0, result.getTranslationX(),          TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(),          TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals( -2.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals( -0.5, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, result.getOrthoX(),                TOLERANCE);
		assertEquals( 0.0, result.getOrthoY(),                TOLERANCE);
		assertEquals( 0.0, result.getOrtho(),                 TOLERANCE);
	}
	
	@Test
	void shearingX() {
		
		double dx = 0.150; // 150nm over 150um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0   ,      0));
		undisplaced.add(Displacement.at(1, 1,      0, 150000,      0+dx, 150000));
		undisplaced.add(Displacement.at(2, 2, 150000, 150000, 150000+dx, 150000));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000   ,      0));
		undisplaced.add(Displacement.at(4, 4,  75000,  75000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		

		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.075, result.getTranslationX(),       TOLERANCE);
		assertEquals( 0.000, result.getTranslationY(),       TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),          TOLERANCE);
		assertEquals( 75000.0, result.getCenterY(),          TOLERANCE);
		
		assertEquals( 0.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals( 0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals( 0.0, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( 0.0, toPPM(result.getOrthoX()),        TOLERANCE);
		assertEquals( 1.0, toPPM(result.getOrthoY()),        TOLERANCE);
		assertEquals( 1.0, toPPM(result.getOrtho()) ,        TOLERANCE);
	}
	
	@Test
	void shearingY() {
		
		double dy = 0.150; // 150nm over 150um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0,      0   ));
		undisplaced.add(Displacement.at(1, 1,      0, 150000,      0, 150000   ));
		undisplaced.add(Displacement.at(2, 2, 150000, 150000, 150000, 150000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000,      0+dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  75000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.000, result.getTranslationX(),        TOLERANCE);
		assertEquals( 0.075, result.getTranslationY(),        TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 75000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getOrthoY()),        TOLERANCE);
		assertEquals(  1.0, toPPM(result.getOrtho()) ,        TOLERANCE);
	}
	
	@Test
	void shearingXY() {
		
		double dx = 0.150; // 150nm over 150um is 1ppm
		double dy = 0.150;
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0   ,      0   ));
		undisplaced.add(Displacement.at(1, 1,      0, 150000,      0+dx, 150000   ));
		undisplaced.add(Displacement.at(2, 2, 150000, 150000, 150000+dx, 150000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000   ,      0+dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  75000, Double.NaN, Double.NaN));
		
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.075, result.getTranslationX(),        TOLERANCE);
		assertEquals( 0.075, result.getTranslationY(),        TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 75000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        TOLERANCE);
		assertEquals(  1.0, toPPM(result.getOrthoY()),        TOLERANCE);
		assertEquals(  2.0, toPPM(result.getOrtho()) ,        TOLERANCE);
		
		result = getCorrectedFirstOrder();
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.075, result.getTranslationX(),        TOLERANCE);
		assertEquals( 0.075, result.getTranslationY(),        TOLERANCE);
		
		assertEquals( 75000.0, result.getCenterX(),           TOLERANCE);
		assertEquals( 75000.0, result.getCenterY(),           TOLERANCE);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getScaleY()),        TOLERANCE);
		assertEquals(  0.0, toPPM(result.getMagnification()), TOLERANCE);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        TOLERANCE);
		assertEquals(  1.0, toPPM(result.getOrthoY()),        TOLERANCE);
		assertEquals(  2.0, toPPM(result.getOrtho()) ,        TOLERANCE);
	}
	
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
		
		run(undisplaced);
		AffineTransform result = getUncorrectedFirstOrder();
		
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( tx, result.getTranslationX(),  TOLERANCE);
		assertEquals( ty, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 70000.0, result.getCenterX(),  TOLERANCE);
		assertEquals( 80000.0, result.getCenterY(),  TOLERANCE);
		
		assertEquals(   0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(   1.000, result.getScaleY()*1E6,        1E-6);
		assertEquals(   0.5  , result.getMagnification()*1E6, 1E-6);
		
		assertEquals(  -1.000, result.getOrthoY()*1E6,        1E-6);
		assertEquals(  -1.000, result.getOrtho()*1E6,         1E-6);
		
		// larger tolerance due to Apache Math Commons Solver
		assertEquals(   0.000, result.getOrthoX()*1E6,        2.6E-11);
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
		
		getRunner().withAlignment(Alignments.ALL)
				   .withCompensations(Compensations.SCALE,Compensations.ORTHO)
				   .run(undisplaced);
		
		AffineTransform result = getUncorrectedFirstOrder();
		
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
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
		
		//
		
		result = getCorrectedFirstOrder();
			
		assertEquals(  0.000, result.getTranslationX(),  TOLERANCE);
		assertEquals(  0.000, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 80000.0, result.getCenterX(),    TOLERANCE);
		assertEquals( 70000.0, result.getCenterY(),    TOLERANCE);
		
		assertEquals(  0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(  0.000, result.getScaleY()*1E6,        1E-6);
		assertEquals(  0.000, result.getMagnification()*1E6, 1E-6);
		
		assertEquals(  0.000, result.getOrthoX()*1E6,        1E-6);
		assertEquals(  0.000, result.getOrthoY()*1E6,        TOLERANCE);
		assertEquals(  0.000, result.getOrtho()*1E6,         1E-6);
	}
	
	/**
	 * Converts affine transform coefficient value into parts-per-million (PPM).
	 * 
	 * @param value, coefficient from affine transform (scale or mag, or ortho)
	 * @return value rounded and scaled to PPM 
	 */
	private double toPPM(double value) {
		BigDecimal bd = BigDecimal.valueOf(value * 1E6);
	    bd = bd.setScale(3, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
