/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration.distortions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	void scalingXY() {
		
		double dx =  0.075; // 150nm over 150um is 1ppm
		double dy = -0.140; // 140nm over 140um is 1ppm
		
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,      0,      0,      0-dx,      0-dy));
		undisplaced.add(Displacement.at(1, 1,      0, 140000,      0-dx, 140000+dy));
		undisplaced.add(Displacement.at(2, 2, 150000, 140000, 150000+dx, 140000+dy));
		undisplaced.add(Displacement.at(3, 3, 150000,      0, 150000+dx,      0-dy));
		undisplaced.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, displacement->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
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
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, displacement->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
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
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, displacement->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
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
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, displacement->true);
		
		assertNotNull(result);
		assertTrue(result.getClass().equals(SimpleAffineTransform.class));
		
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
