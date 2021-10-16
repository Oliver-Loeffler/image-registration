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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.alignment.SimpleRigidTransform;
import net.raumzeitfalle.registration.alignment.SkipRigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.solvertest.NumericsTestBase;

/**
 * Holds all common test cases for rigid transform calculations AND corrections.
 */
public class RigidTransformNumerics extends NumericsTestBase {

	private static final double TOLERANCE = 1E-11;
	
	public void translationX() {

		List<Displacement> displacements = listOf(
				Displacement.at(0, 0, 1000, 1000, 1010, 1000),
				Displacement.at(1, 1, 1000, 9000, 1010, 9000),
				Displacement.at(2, 2, 9000, 9000, 9010, 9000),
				Displacement.at(3, 3, 9000, 1000, 9010, 1000));

		run(displacements);

		RigidTransform result = getUncorrectedAlignment();

		assertEquals(SimpleRigidTransform.class, result.getClass());

		assertEquals(10.0, result.getTranslationX(), TOLERANCE);
		assertEquals(0.0, result.getTranslationY(), TOLERANCE);
		assertEquals(0.0, result.getRotation(), TOLERANCE);

	}
	
	public void translationY() {
		
		List<Displacement> displacements = listOf(
				Displacement.at(0, 0, 1000, 1000, 1000,  990),
				Displacement.at(1, 1, 1000, 9000, 1000, 8990),
				Displacement.at(2, 2, 9000, 9000, 9000, 8990),
				Displacement.at(3, 3, 9000, 1000, 9000,  990));
		
		run(displacements);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
		assertEquals(   0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( -10.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	public void translationXY() {
		
		List<Displacement> displaced = listOf(
				Displacement.at(0, 0, 1000, 1000, 1010,  990),
				Displacement.at(1, 1, 1000, 9000, 1010, 8990),
				Displacement.at(2, 2, 9000, 9000, 9010, 8990),
				Displacement.at(3, 3, 9000, 1000, 9010,  990));
		
		run(displaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
		assertEquals(  10.0, result.getTranslationX(), TOLERANCE);
		assertEquals( -10.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	public void rotation() {
		
		
		double rotated = Math.sqrt(2)*1E3;
		
		List<Displacement> displaced =listOf(
				Displacement.at(0, 0, -1000, -1000,        0,  -rotated),
				Displacement.at(0, 0, -1000,  1000, -rotated,         0),
				Displacement.at(0, 0,  1000,  1000,        0,   rotated),
				Displacement.at(0, 0,  1000, -1000,  rotated,         0));
		
		run(displaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
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
	
	public void rotationAndTranslation() {
		
		
		double rotated = Math.sqrt(2)*1E3;
		
		List<Displacement> displaced = listOf(
				Displacement.at(0, 0, -1000, -1000,        0+10,  -rotated-4),
				Displacement.at(0, 0, -1000,  1000, -rotated+10,         0-4),
				Displacement.at(0, 0,  1000,  1000,        0+10,   rotated-4),
				Displacement.at(0, 0,  1000, -1000,  rotated+10,         0-4));
		
		run(displaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
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
	
	public void skipTransform() {
		
		List<Displacement> displacements = listOf(
				Displacement.at(0, 0, 1000, 1000, 1010,  990),
				Displacement.at(1, 1, 1000, 9000, 1010, 8990),
				Displacement.at(2, 2, 9000, 9000, 9010, 8990),
				Displacement.at(3, 3, 9000, 1000, 9010,  990));
	
		Predicate<Displacement> selectNone = d -> false; // no displacement will be used
		
		getRunner().selectForRemoval(selectNone.negate())
				   .run(displacements);
		
		run(displacements);
		
		RigidTransform result = getCorrectedAlignment();
		
		assertNotNull(result);
		assertEquals(SkipRigidTransform.class, result.getClass());
	
		assertEquals( 0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( 0.0, result.getTranslationY(), TOLERANCE);
		assertEquals( 0.0, result.getRotation(),     TOLERANCE);
	
	}
	
	public void translationXonly1D() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, 1010, Double.NaN));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, 1010, Double.NaN));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, 9010, Double.NaN));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, 9010, Double.NaN));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
		assertEquals(  10.0, result.getTranslationX(), TOLERANCE);
		assertEquals(   0.0, result.getTranslationY(), TOLERANCE);
		assertEquals(   0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	public void translationYonly1D() {
		
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0, 1000, 1000, Double.NaN, 1000));
		undisplaced.add(Displacement.at(1, 1, 1000, 9000, Double.NaN, 9100));
		undisplaced.add(Displacement.at(2, 2, 9000, 9000, Double.NaN, 9100));
		undisplaced.add(Displacement.at(3, 3, 9000, 1000, Double.NaN, 1000));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
		assertEquals(  0.0, result.getTranslationX(), TOLERANCE);
		assertEquals( 50.0, result.getTranslationY(), TOLERANCE);
		assertEquals(  0.0, result.getRotation(),     TOLERANCE);
		
	}
	
	public void displacementsAlongHorizontalLine() {
		
		// 1ppm rotation	
		List<Displacement> undisplaced = new ArrayList<>(5);
		undisplaced.add(Displacement.at(0, 0,  20000, 70000,  20000-.06, 70000-.06));
		undisplaced.add(Displacement.at(1, 1,  40000, 70000,  40000-.04, 70000-.04));
		undisplaced.add(Displacement.at(2, 2,  60000, 70000,  60000-.02, 70000-.02));
		undisplaced.add(Displacement.at(3, 3,  80000, 70000,  80000+.00, 70000+.00));
		undisplaced.add(Displacement.at(4, 4, 100000, 70000, 100000+.02, 70000+.02));
		undisplaced.add(Displacement.at(5, 5, 120000, 70000, 120000+.04, 70000+.04));
		undisplaced.add(Displacement.at(6, 6, 140000, 70000, 140000+.06, 70000+.06));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		assertEquals(SimpleRigidTransform.class, result.getClass());
		
		assertEquals(  69.999 , result.getTranslationX()*1E3, 1E-3);
		assertEquals( -79.999 , result.getTranslationY()*1E3, 1E-3);
		assertEquals(    1E-6 , result.getRotation(),     TOLERANCE);
		
	}
	
	public void singularityXY() {
			
		double dx =   0.075; 
		double dy =  -0.075; 
	
		List<Displacement> undisplaced = listOf(Displacement.at(0, 0, 4500, 4500, 4500+dx, 4500+dy));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		
		assertEquals(  75.0 , result.getTranslationX()*1E3, 1E-3);
		assertEquals( -75.0 , result.getTranslationY()*1E3, 1E-3);
		assertEquals(   0.0 , result.getRotation(),     TOLERANCE);
		
	}
	
	public void singularityX() {
			
		double dx =   0.075; 
	
		List<Displacement> undisplaced = listOf(Displacement.at(0, 0, 4500, Double.NaN, 4500+dx, Double.NaN));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		
		assertEquals(  75.0 , result.getTranslationX()*1E3, 1E-3);
		assertEquals(   0.0 , result.getTranslationY()*1E3, 1E-3);
		assertEquals(   0.0 , result.getRotation(),     TOLERANCE);
		
	}
	
	public void singularityY() {
			 
		double dy =  -0.075; 
	
		List<Displacement> undisplaced = listOf(Displacement.at(0, 0, Double.NaN, 4500, Double.NaN, 4500+dy));
		
		run(undisplaced);
		
		RigidTransform result = getUncorrectedAlignment();
		
		assertNotNull(result);
		
		assertEquals(   0.0 , result.getTranslationX()*1E3, 1E-3);
		assertEquals( -75.0 , result.getTranslationY()*1E3, 1E-3);
		assertEquals(   0.0 , result.getRotation(),     TOLERANCE);
		
	}
	
}
