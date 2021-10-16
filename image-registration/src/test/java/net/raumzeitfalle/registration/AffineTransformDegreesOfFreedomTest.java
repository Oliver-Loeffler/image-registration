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
package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.alignment.SimpleTranslation;
import net.raumzeitfalle.registration.alignment.Translation;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.AffineTransformCalculation;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;

class AffineTransformDegreesOfFreedomTest {
	
	private DegreesOfFreedom classUnderTest;
	
	@BeforeEach
	public void prepare() {
		 classUnderTest = new DegreesOfFreedom();
	}
	
	@Test
	void translation_scaling_skew_enabled_XY() {
		
		/*
		 * 6 Parameters:  -> requires 6 equations (3 x-equations, 3 y-equations)
		 * 
		 * translation(x,y)
		 * scale(x,y)
		 * ortho(x,y)
		 * 
		 */
		List<Displacement> undisplaced = new ArrayList<>(4);
		undisplaced.add(Displacement.at(0, 0,    0,    0,    0,    0));
		undisplaced.add(Displacement.at(1, 1,    0, 8000,    0, 8000));
		undisplaced.add(Displacement.at(2, 2, 8000, 8000, 8000, 8000));

		Translation translation = SimpleTranslation.with(0.010, -0.020);
		AffineTransform mytransform = SimpleAffineTransform.with(translation,
				-1E-6, -2E-6, -1E-6, -3E-6, 0, 0);
		
		List<Displacement> displaced = undisplaced.stream()
												  .map(mytransform)
												  .collect(Collectors.toList());
		
		displaced.forEach(classUnderTest);
		
		assertEquals( 2, classUnderTest.getX(), "degree of freedom in X");
		assertEquals( 2, classUnderTest.getY(), "degree of freedom in Y");
		assertEquals( 2, classUnderTest.getCombined(), "degree of freedom in XY");
		
		AffineTransform transform = calculateTransform(displaced);
		
		assertNotNull(transform);
		
	}

	private AffineTransform calculateTransform(List<Displacement> undisplaced) {
		return new AffineTransformCalculation().apply(undisplaced, d->true);
	}
	
	public List<Displacement> listOf(Displacement... d) {
		return Arrays.asList(d);
	}

}
