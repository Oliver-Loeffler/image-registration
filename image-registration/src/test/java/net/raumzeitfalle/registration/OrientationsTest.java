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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrientationsTest {

	private Orientations classUnderTest = new Orientations();
	
	private Orientation orientation;
	
	@Test
	void x() {
		
		List<Integer> xValues = List.of(1,2,3,4,5);
		List<Integer> yValues = List.of();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.X, orientation),
				()->assertEquals(1, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("X", orientation.toString(), "Name")
				);
	}
	
	@Test
	void y() {
		
		List<Integer> yValues = List.of(1,2,3,4,5);
		List<Integer> xValues = List.of();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.Y, orientation),
				()->assertEquals(1, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("Y", orientation.toString(), "Name")
				);
	}

	@Test
	void empty_xy() {
		
		List<Integer> yValues = Collections.emptyList();
		List<Integer> xValues = Collections.emptyList();
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.XY, orientation),
				()->assertEquals(2, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("XY", orientation.toString(), "Name")
				);
	}
	
	@Test
	void populated_xy() {
		
		List<Integer> yValues = List.of(1,2,3,4,5);
		List<Integer> xValues = List.of(4,5);
		
		orientation = classUnderTest.determine(xValues, yValues);
		
		assertAll(				
				()->assertEquals(Orientations.XY, orientation),
				()->assertEquals(2, orientation.getDimensions(), "# of dimensions"),
				()->assertEquals("XY", orientation.toString(), "Name")
				);
	}
}
