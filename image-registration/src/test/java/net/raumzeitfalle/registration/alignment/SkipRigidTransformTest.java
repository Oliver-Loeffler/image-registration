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
package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.Transform;
import net.raumzeitfalle.registration.displacement.Displacement;

class SkipRigidTransformTest {
	
	private final Transform classUnderTest = new SkipRigidTransform();

	@Test
	void skip() {
		assertTrue(classUnderTest.skip());
	}
	
	@Test
	void rotation() {
		assertEquals(0.0, ((RigidTransform) classUnderTest).getRotation(), 1E-11);
	}
	
	@Test
	void translation() {
		assertEquals(0.0, ((RigidTransform) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(0.0, ((RigidTransform) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void apply() {
		Displacement source = Displacement.at(0, 0, 10, 20);
		Displacement result = classUnderTest.apply(source);
		
		assertEquals( source, result );
		assertEquals( source.hashCode(), result.hashCode() );
	}
}
