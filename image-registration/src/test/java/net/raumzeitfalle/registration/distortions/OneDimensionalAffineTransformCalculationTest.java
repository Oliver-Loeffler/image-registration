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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.Disabled;
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
	
	@Disabled("Functionality to be implemented")
	@Test
	void displacementsAlongVerticalLine_oneDimensional_y() {
		// 1ppm rotation

		double ty = -.01;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0, 70000,  20000, Double.NaN,  20000-.06+ty));
		undisplaced.add(Displacement.at(1, 1, 70000,  40000, Double.NaN,  40000-.04+ty));
		undisplaced.add(Displacement.at(2, 2, 70000,  60000, Double.NaN,  60000-.02+ty));
		undisplaced.add(Displacement.at(3, 3, 70000,  80000, Double.NaN,  80000    +ty));
		undisplaced.add(Displacement.at(4, 4, 70000, 100000, Double.NaN, 100000+.02+ty));
		undisplaced.add(Displacement.at(5, 5, 70000, 120000, Double.NaN, 120000+.04+ty));
		undisplaced.add(Displacement.at(6, 6, 70000, 140000, Double.NaN, 140000+.06+ty));
		
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( 0d, result.getTranslationX(),  TOLERANCE);
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
	
	@Disabled("Not yet implemented")
	@Test
	void displacementsAlongVerticalLine_oneDimensional_x() {
		// 1ppm rotation

		double tx = 0.1;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0, 70000,  20000, 70000+.06+tx, Double.NaN));
		undisplaced.add(Displacement.at(1, 1, 70000,  40000, 70000+.04+tx, Double.NaN));
		undisplaced.add(Displacement.at(2, 2, 70000,  60000, 70000+.02+tx, Double.NaN));
		undisplaced.add(Displacement.at(3, 3, 70000,  80000, 70000    +tx, Double.NaN));
		undisplaced.add(Displacement.at(4, 4, 70000, 100000, 70000-.02+tx, Double.NaN));
		undisplaced.add(Displacement.at(5, 5, 70000, 120000, 70000-.04+tx, Double.NaN));
		undisplaced.add(Displacement.at(6, 6, 70000, 140000, 70000-.06+tx, Double.NaN));
		
		
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
		assertEquals(SimpleAffineTransform.class, result.getClass());
		
		assertEquals( tx, result.getTranslationX(),  TOLERANCE);
		assertEquals( 0d, result.getTranslationY(),  TOLERANCE);
		
		assertEquals( 70000.0, result.getCenterX(),  TOLERANCE);
		assertEquals( 80000.0, result.getCenterY(),  TOLERANCE);
		
		assertEquals(   0.000, result.getScaleX()*1E6,        1E-6);
		assertEquals(   0.000, result.getScaleY()*1E6,        1E-6);
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
	}
	
	@Disabled("Not yet implemented")
	@Test
	void displacementsAlongHorizontalLine_oneDimensional_x() {
		// 1ppm rotation

		double tx = 0.1;
		double ty = -.01;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,  20000, 70000,  20000-.06+tx, Double.NaN));
		undisplaced.add(Displacement.at(1, 1,  40000, 70000,  40000-.04+tx, Double.NaN));
		undisplaced.add(Displacement.at(2, 2,  60000, 70000,  60000-.02+tx, Double.NaN));
		undisplaced.add(Displacement.at(3, 3,  80000, 70000,  80000+.00+tx, Double.NaN));
		undisplaced.add(Displacement.at(4, 4, 100000, 70000, 100000+.02+tx, Double.NaN));
		undisplaced.add(Displacement.at(5, 5, 120000, 70000, 120000+.04+tx, Double.NaN));
		undisplaced.add(Displacement.at(6, 6, 140000, 70000, 140000+.06+tx, Double.NaN));
			
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
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
	}
	
	@Disabled("Not yet implemented")
	@Test
	void displacementsAlongHorizontalLine_oneDimensional_y() {
		// 1ppm rotation

		double tx = 0.1;
		double ty = -.01;
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,  20000, 70000, Double.NaN, 70000-.06+ty));
		undisplaced.add(Displacement.at(1, 1,  40000, 70000, Double.NaN, 70000-.04+ty));
		undisplaced.add(Displacement.at(2, 2,  60000, 70000, Double.NaN, 70000-.02+ty));
		undisplaced.add(Displacement.at(3, 3,  80000, 70000, Double.NaN, 70000+.00+ty));
		undisplaced.add(Displacement.at(4, 4, 100000, 70000, Double.NaN, 70000+.02+ty));
		undisplaced.add(Displacement.at(5, 5, 120000, 70000, Double.NaN, 70000+.04+ty));
		undisplaced.add(Displacement.at(6, 6, 140000, 70000, Double.NaN, 70000+.06+ty));
			
		AffineTransform result = funtionUnderTest.apply(undisplaced, d->true);
		
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
	}
}
