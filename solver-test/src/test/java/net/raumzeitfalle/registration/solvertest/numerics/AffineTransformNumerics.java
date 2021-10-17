/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.solvertest.numerics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;
import net.raumzeitfalle.registration.firstorder.Alignments;
import net.raumzeitfalle.registration.firstorder.Compensations;
import net.raumzeitfalle.registration.solvertest.NumericsTestBase;

/**
 * Holds all common test cases for affine transform calculations AND corrections.
 */
public class AffineTransformNumerics extends NumericsTestBase {
	
	private final double tolerance;

	public AffineTransformNumerics() {
	    this(1E-11);
	}
	
	public AffineTransformNumerics(double tolerance) {
	    this.tolerance = tolerance;
	}
	
	public void assertZeroTransform() {
		
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
		
		assertEquals( 0.0, result.getTranslationX(),  tolerance);
		assertEquals( 0.0, result.getTranslationY(),  tolerance);
		
		assertEquals( 4500.0, result.getCenterX(),    tolerance);
		assertEquals( 4000.0, result.getCenterY(),    tolerance);
		
		assertEquals( 0.0, result.getScaleX(),        tolerance);
		assertEquals( 0.0, result.getScaleY(),        tolerance);
		assertEquals( 0.0, result.getMagnification(), tolerance);
		
		assertEquals( 0.0, result.getOrthoX(),        tolerance);
		assertEquals( 0.0, result.getOrthoY(),        tolerance);
		assertEquals( 0.0, result.getOrtho(),         tolerance);
	}
	
	public void assertScalingX() {
		
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
		
		assertEquals( 0.0, result.getTranslationX(),          tolerance);
		assertEquals( 0.0, result.getTranslationY(),          tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 70000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals(  0.5, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( 0.0, result.getOrthoX(),                tolerance);
		assertEquals( 0.0, result.getOrthoY(),                tolerance);
		assertEquals( 0.0, result.getOrtho(),                 tolerance);
	}
	
	public void assert_scalingY_withoutX() {
		
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
		
		assertEquals( 0.0, result.getTranslationX(),          tolerance);
		assertEquals( 0.0, result.getTranslationY(),          tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 70000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals( -2.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals( -1.0, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( 0.0, result.getOrthoX(),                tolerance);
		assertEquals( 0.0, result.getOrthoY(),                tolerance);
		assertEquals( 0.0, result.getOrtho(),                 tolerance);
	}
	
	public void assert_scalingX_withoutY() {
		
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
		
		assertEquals( 0.0, result.getTranslationX(),          tolerance);
		assertEquals( 0.0, result.getTranslationY(),          tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 70000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals(  0.5, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( 0.0, result.getOrthoX(),                tolerance);
		assertEquals( 0.0, result.getOrthoY(),                tolerance);
		assertEquals( 0.0, result.getOrtho(),                 tolerance);
	}
	
	public void assertScalingXY() {
		
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
		
		assertEquals( 0.0, result.getTranslationX(),          tolerance);
		assertEquals( 0.0, result.getTranslationY(),          tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 70000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  1.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals( -2.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals( -0.5, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( 0.0, result.getOrthoX(),                tolerance);
		assertEquals( 0.0, result.getOrthoY(),                tolerance);
		assertEquals( 0.0, result.getOrtho(),                 tolerance);
	}
	
	public void assertShearingX() {
		
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
		
		assertEquals( 0.075, result.getTranslationX(),       tolerance);
		assertEquals( 0.000, result.getTranslationY(),       tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),          tolerance);
		assertEquals( 75000.0, result.getCenterY(),          tolerance);
		
		assertEquals( 0.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals( 0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals( 0.0, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( 0.0, toPPM(result.getOrthoX()),        tolerance);
		assertEquals( 1.0, toPPM(result.getOrthoY()),        tolerance);
		assertEquals( 1.0, toPPM(result.getOrtho()) ,        tolerance);
	}
	
	public void assertShearingY() {
		
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
		
		assertEquals( 0.000, result.getTranslationX(),        tolerance);
		assertEquals( 0.075, result.getTranslationY(),        tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 75000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals(  0.0, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getOrthoY()),        tolerance);
		assertEquals(  1.0, toPPM(result.getOrtho()) ,        tolerance);
	}
	
	public void assertShearingXY() {
		
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
		
		assertEquals( 0.075, result.getTranslationX(),        tolerance);
		assertEquals( 0.075, result.getTranslationY(),        tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 75000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals(  0.0, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        tolerance);
		assertEquals(  1.0, toPPM(result.getOrthoY()),        tolerance);
		assertEquals(  2.0, toPPM(result.getOrtho()) ,        tolerance);
		
		result = getCorrectedFirstOrder();
		
		assertNotNull(result);
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0.075, result.getTranslationX(),        tolerance);
		assertEquals( 0.075, result.getTranslationY(),        tolerance);
		
		assertEquals( 75000.0, result.getCenterX(),           tolerance);
		assertEquals( 75000.0, result.getCenterY(),           tolerance);
		
		assertEquals(  0.0, toPPM(result.getScaleX()),        tolerance);
		assertEquals(  0.0, toPPM(result.getScaleY()),        tolerance);
		assertEquals(  0.0, toPPM(result.getMagnification()), tolerance);
		
		assertEquals( -1.0, toPPM(result.getOrthoX()),        tolerance);
		assertEquals(  1.0, toPPM(result.getOrthoY()),        tolerance);
		assertEquals(  2.0, toPPM(result.getOrtho()) ,        tolerance);
	}
	
	public void assertDisplacementsAlongVerticalLine() {
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
		
		assertEquals( tx, result.getTranslationX(),  tolerance);
		assertEquals( ty, result.getTranslationY(),  tolerance);
		
		assertEquals( 70000.0, result.getCenterX(),  tolerance);
		assertEquals( 80000.0, result.getCenterY(),  tolerance);
		
		assertEquals(   0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(   1.000, result.getScaleY()*1E6,        1E-6);
		assertEquals(   0.5  , result.getMagnification()*1E6, 1E-6);
		
		assertEquals(  -1.000, result.getOrthoY()*1E6,        1E-6);
		assertEquals(  -1.000, result.getOrtho()*1E6,         1E-6);
		
		// larger tolerance due to Apache Math Commons Solver
		assertEquals(   0.000, result.getOrthoX()*1E6,        2.6E-11);
	}
	
	public void assertDisplacementsAlongHorizontalLine() {
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
		
		assertEquals(  tx, result.getTranslationX(),  tolerance);
		assertEquals(  ty, result.getTranslationY(),  tolerance);
		
		assertEquals( 80000.0, result.getCenterX(),    tolerance);
		assertEquals( 70000.0, result.getCenterY(),    tolerance);
		
		assertEquals(  1.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(  0.0,   result.getScaleY()*1E6,        1E-6);
		assertEquals(  0.500, result.getMagnification()*1E6, 1E-6);
		
		assertEquals( -1.000, result.getOrthoX()*1E6,        1E-6);
		assertEquals(  0.000, result.getOrthoY()*1E6,        tolerance);
		assertEquals(  1.000, result.getOrtho()*1E6,         1E-6);
		
		//
		
		result = getCorrectedFirstOrder();
			
		assertEquals(  0.000, result.getTranslationX(),  tolerance);
		assertEquals(  0.000, result.getTranslationY(),  tolerance);
		
		assertEquals( 80000.0, result.getCenterX(),    tolerance);
		assertEquals( 70000.0, result.getCenterY(),    tolerance);
		
		assertEquals(  0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(  0.000, result.getScaleY()*1E6,        1E-6);
		assertEquals(  0.000, result.getMagnification()*1E6, 1E-6);
		
		assertEquals(  0.000, result.getOrthoX()*1E6,        1E-6);
		assertEquals(  0.000, result.getOrthoY()*1E6,        tolerance);
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
