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
