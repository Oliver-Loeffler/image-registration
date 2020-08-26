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

class SimpleRigidTransformTest {

	private final double TOLERANCE = 1E-11;
	
	private Transform classUnderTest;

	@Test
	void skip() {
		classUnderTest = SimpleRigidTransform.with(10, -20, 3);
		assertFalse(classUnderTest.skip());
	}
	
	@Test
	void rotation() {
		classUnderTest = SimpleRigidTransform.with(10, -20, 3);
		assertEquals( 3.0, ((RigidTransform) classUnderTest).getRotation(), 1E-11);
	}
	
	@Test
	void translation() {
		
		classUnderTest = SimpleRigidTransform.with(10, -20, 3);
		assertEquals( 10.0, ((RigidTransform) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-20.0, ((RigidTransform) classUnderTest).getTranslationY(), 1E-11);
		
		Translation trans = ((RigidTransform) classUnderTest).getTranslation();
		assertEquals( 10, trans.getTranslationX(), TOLERANCE);
		assertEquals(-20, trans.getTranslationY(), TOLERANCE);
		
	}

	@Test
	void apply() {
		classUnderTest = SimpleRigidTransform.with(10, -20, 3);
		
		Displacement source = Displacement.at(0, 0, 10, 20);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( 60, result.getXd(), TOLERANCE);
		assertEquals( 10, result.getYd(), TOLERANCE);
	}
	
	@Test
	void withTranslation() {
		classUnderTest = SimpleRigidTransform.with(Translation.of(10, -10));
		
		Displacement source = Displacement.at(0, 0, 10, -10);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( 0.0, result.getXd(), TOLERANCE);
		assertEquals( 0.0, result.getYd(), TOLERANCE);
	}
	
	@Test
	void withTranslationAndRotation() {
		
		classUnderTest = SimpleRigidTransform.with(Translation.of(10, -10), 7);
		
		Displacement source = Displacement.at(0, 0, 10, -10);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( -70.0, result.getXd(), TOLERANCE);
		assertEquals( -70.0, result.getYd(), TOLERANCE);
	}
	
	@Test
	void toStringMethod() {
		classUnderTest = SimpleRigidTransform.with(10, -20, 3);
		
		assertEquals("RigidTransform [x=10.0000000, y=-20.0000000, rotation=3000000.0000000 urad]", classUnderTest.toString());		
	}

}
