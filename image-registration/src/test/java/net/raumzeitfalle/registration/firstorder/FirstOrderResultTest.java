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
package net.raumzeitfalle.registration.firstorder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.SimpleAffineTransform;

class FirstOrderResultTest {
	
	private FirstOrderResult classUnderTest;
	
	private RigidTransform rigid = RigidTransform.with(10.0, 20.0, 42.17);
	
	private AffineTransform affine = SimpleAffineTransform.with(rigid.getTranslation(), 0d,0d,0d,0d,0d,0d);

	private List<Displacement> emptyListOfDisplacements = Collections.emptyList();
	
	@Test
	void thatRigidTransformNullThrowsException() {
		
		
		Throwable t = assertThrows(NullPointerException.class,
				()->new FirstOrderResult(null, affine, emptyListOfDisplacements));
				

		assertEquals("Alignment (RigidTransform) must not be null.", t.getMessage());
		
	}
	
	@Test
	void thatAffineTransformNullThrowsException() {
		
		Throwable t = assertThrows(NullPointerException.class,
				()->new FirstOrderResult(rigid, null, emptyListOfDisplacements));
				

		assertEquals("FirstOrder (AffineTransform) must not be null.", t.getMessage());
		
	}
	
	@Test
	void thatDisplacementListNullThrowsException() {
		
		Throwable t = assertThrows(NullPointerException.class,
				()->new FirstOrderResult(rigid, affine, null));
				

		assertEquals("Collection of Displacements (results) must not be null.", t.getMessage());
		
	}
	
	@Test
	void succesfulCreation() {
		
		Collection<Displacement> samples = Arrays.asList(Displacement.at(1, 1, 0.19, 1.21));
		
		classUnderTest = new FirstOrderResult(rigid, affine, samples);
		
		assertNotNull(classUnderTest);
		
		assertEquals(rigid, classUnderTest.getAlignment(), "Alignment Transform (Rigid Transform)");
		assertEquals(affine, classUnderTest.getFirstOrder(), "Affine Transform (First order scale-ortho model parameters)");
		assertEquals(samples, classUnderTest.getDisplacements(), "Collection of displacements");

	}

}
