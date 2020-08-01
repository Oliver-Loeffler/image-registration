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
package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DimensionTest {
	
	private Dimension<Orientable> classUnderTest;

	@Test
	void accept() {
		
		classUnderTest = new Dimension<>();
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.UNKNOWN, classUnderTest.getDirection());
		assertEquals(0, classUnderTest.getDimensions());
		
		classUnderTest.accept(()->Orientation.X);
		
		assertEquals(1, classUnderTest.getXCoordinateCount());
		assertEquals(0, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.X, classUnderTest.getDirection());
		assertEquals(1, classUnderTest.getDimensions());
		
		classUnderTest.accept(()->Orientation.Y);
		classUnderTest.accept(()->Orientation.X);
		
		assertEquals(2, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.XY, classUnderTest.getDirection());
		assertEquals(2, classUnderTest.getDimensions());
		
		assertEquals("Dimension [XY (x=2,y=1)]", classUnderTest.toString());
		
		classUnderTest = new Dimension<>();
		classUnderTest.accept(()->Orientation.Y);
		
		assertEquals(0, classUnderTest.getXCoordinateCount());
		assertEquals(1, classUnderTest.getYCoordinateCount());
		assertEquals(Orientation.Y, classUnderTest.getDirection());
		assertEquals(1, classUnderTest.getDimensions());
		
	}
	
}
