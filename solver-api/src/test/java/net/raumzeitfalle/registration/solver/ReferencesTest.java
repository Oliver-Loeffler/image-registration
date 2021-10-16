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
package net.raumzeitfalle.registration.solver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReferencesTest {

	@Test
	void test() {
		
		double[][] design = { 
				{ -75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ -75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, 75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, 70000.0, 1.0, 0.0 }, 
				{ 0.0, 70000.0, -75000.0, 0.0, 0.0, 1.0 },
				{ 75000.0, 0.0, 0.0, -70000.0, 1.0, 0.0 }, 
				{ 0.0, -70000.0, -75000.0, 0.0, 0.0, 1.0 } };
		
		References references = ()->design;
		
		
		assertAll(
				()->assertEquals(8, references.rows(), "row count"),
				()->assertArrayEquals(design, references.getArray(), "design arrays must be equal")
				);
	}
	


}
