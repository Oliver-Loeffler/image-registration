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
package net.raumzeitfalle.registration.displacement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class CategoryTest {

	@Test
	void values() {
		
		assertEquals( 3, Category.values().length );
		
		Set<Category> values = EnumSet.allOf(Category.class);
		
		assertEquals( 3, values.size() );
		
		assertTrue(values.contains(Category.ALIGN));
		assertTrue(values.contains(Category.REG));
		assertTrue(values.contains(Category.INFO_ONLY));
		
	}
	
	@Test
	void fromString() {
		
		assertEquals(Category.REG, Category.fromString(null));
		assertEquals(Category.REG, Category.fromString(""));
		assertEquals(Category.REG, Category.fromString("whatever"));
		assertEquals(Category.REG, Category.fromString("imagereG_istration"));
		assertEquals(Category.ALIGN, Category.fromString("selectedaliGNment"));
		assertEquals(Category.INFO_ONLY, Category.fromString("forInFOrmation"));
		
	}

}
