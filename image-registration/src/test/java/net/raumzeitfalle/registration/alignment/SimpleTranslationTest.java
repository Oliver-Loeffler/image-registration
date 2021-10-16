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
package net.raumzeitfalle.registration.alignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class SimpleTranslationTest {

	private Translation classUnderTest = null;
	

	@Test
	void skip() {
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertFalse(classUnderTest.skip());
	}
		
	@Test
	void translation() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertEquals( 10.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-20.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}
	
	@Test
	void shiftx() {
		
		classUnderTest = SimpleTranslation.shiftX(10.0);
		
		assertEquals( 10.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(  0.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}
	
	@Test
	void shifty() {
		
		classUnderTest = SimpleTranslation.shiftY(-22.0);
		
		assertEquals(  0.0, ((Translation) classUnderTest).getTranslationX(), 1E-11);
		assertEquals(-22.0, ((Translation) classUnderTest).getTranslationY(), 1E-11);
	}

	@Test
	void apply() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		double x = 20;
		double y = 30;
		
		Displacement source = Displacement.at(0, 0, x, y);
		Displacement result = classUnderTest.apply(source);
		
		assertNotEquals( source, result );
		assertNotEquals( source.hashCode(), result.hashCode() );
		
		assertEquals( 10, result.getXd());
		assertEquals( 50, result.getYd());
	}
	
	@Test
	void toStringMethod() {
		
		classUnderTest = SimpleTranslation.with(10, -20);
		
		assertEquals("Translation [x=10.0000000, y=-20.0000000]", classUnderTest.toString());
		
	}

}
