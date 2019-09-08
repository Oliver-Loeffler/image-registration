package net.raumzeitfalle.registration.displacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class DisplacementTest {

	private Displacement classUnderTest;

	@Test
	void undisplaced() {
		classUnderTest = Displacement.at(1, 2, 10, 20);

		assertEquals(Category.REG, classUnderTest.getCategory());

		assertEquals(10.0, classUnderTest.getX(), 1E-3);
		assertEquals(20.0, classUnderTest.getY(), 1E-3);

		assertEquals(10.0, classUnderTest.getXd(), 1E-3);
		assertEquals(20.0, classUnderTest.getYd(), 1E-3);

		assertEquals(0.0, classUnderTest.dX(), 1E-3);
		assertEquals(0.0, classUnderTest.dY(), 1E-3);

		assertEquals(1, classUnderTest.getIndex());
		assertEquals(2, classUnderTest.getId());
	}

	@Test
	void fromDisplacement() {

		Displacement source = Displacement.at(1, 2, 10, 20);
		classUnderTest = Displacement.from(source, 11, 22);

		assertNotEquals(source, classUnderTest);

		assertEquals(10.0, classUnderTest.getX(), 1E-3);
		assertEquals(20.0, classUnderTest.getY(), 1E-3);

		// actual displaced value
		assertEquals(11.0, classUnderTest.getXd(), 1E-3);
		assertEquals(22.0, classUnderTest.getYd(), 1E-3);

		// the displacement
		assertEquals(1.0, classUnderTest.dX(), 1E-3);
		assertEquals(2.0, classUnderTest.dY(), 1E-3);

		assertEquals(1, classUnderTest.getIndex());
		assertEquals(2, classUnderTest.getId());

	}

	@Test
	void moveBy() {

		Displacement source = Displacement.at(1, 2, 10, 20, 11, 22, Category.INFO_ONLY);
		classUnderTest = source.moveBy(-10, -20);

		assertNotEquals(source, classUnderTest);

		// the design value - moved
		assertEquals(0.0, classUnderTest.getX(), 1E-3);
		assertEquals(0.0, classUnderTest.getY(), 1E-3);

		// actual displaced value - moved
		assertEquals(1.0, classUnderTest.getXd(), 1E-3);
		assertEquals(2.0, classUnderTest.getYd(), 1E-3);

		// the displacement
		assertEquals(1.0, classUnderTest.dX(), 1E-3);
		assertEquals(2.0, classUnderTest.dY(), 1E-3);

		assertEquals(1, classUnderTest.getIndex());
		assertEquals(2, classUnderTest.getId());

		assertEquals(Category.INFO_ONLY, classUnderTest.getCategory());
	}

	@Test
	void correctBy() {

		Displacement source = Displacement.at(1, 2, 10, 20, 11, 22, Category.INFO_ONLY);
		classUnderTest = source.correctBy(1, 2);

		assertNotEquals(source, classUnderTest);

		assertEquals(10.0, classUnderTest.getX(), 1E-3);
		assertEquals(20.0, classUnderTest.getY(), 1E-3);

		// actual displaced value but corrected
		assertEquals(10.0, classUnderTest.getXd(), 1E-3);
		assertEquals(20.0, classUnderTest.getYd(), 1E-3);

		// the displacement, now corrected
		assertEquals(0.0, classUnderTest.dX(), 1E-3);
		assertEquals(0.0, classUnderTest.dY(), 1E-3);

		assertEquals(1, classUnderTest.getIndex());
		assertEquals(2, classUnderTest.getId());

		assertEquals(Category.INFO_ONLY, classUnderTest.getCategory());
	}

	@Test
	void belongsTo() {
		classUnderTest = Displacement.at(1, 2, 10, 20, 11, 22, Category.INFO_ONLY);

		assertTrue(classUnderTest.belongsTo(Category.INFO_ONLY));
		assertFalse(classUnderTest.belongsTo(Category.REG));

		classUnderTest = Displacement.at(1, 2, 10, 20, 11, 23);

		assertTrue(classUnderTest.belongsTo(Category.REG));
		assertFalse(classUnderTest.belongsTo(Category.ALIGN));

		classUnderTest = Displacement.at(1, 2, 10, 20, 11, 22, Category.ALIGN);

		assertFalse(classUnderTest.belongsTo(Category.INFO_ONLY));
		assertFalse(classUnderTest.belongsTo(Category.REG));
		assertTrue(classUnderTest.belongsTo(Category.ALIGN));
	}
	
	@Test
	void average() {
		List<Displacement> displaced = new ArrayList<>(4);
		displaced.add(Displacement.at(0, 0, 1000, 2000, 1010,   99));
		displaced.add(Displacement.at(1, 1, 1000, 9000, 1010, 8990));
		displaced.add(Displacement.at(2, 2, 9000, 9000, 9010, 8990));
		displaced.add(Displacement.at(3, 3, 9000, 2000, 9010,   99));
		
		double meanX = Displacement.average(displaced, d->true, Displacement::getX);
		assertEquals(5000, meanX, 1E-1);
		
		double meanY = Displacement.average(displaced, d->true, Displacement::getY);
		assertEquals(5500, meanY, 1E-1);
		
		double meanXd = Displacement.average(displaced, d->true, Displacement::getXd);
		assertEquals(5010, meanXd, 1E-1);
		
		double meanYd = Displacement.average(displaced, d->true, Displacement::getYd);
		assertEquals(4544.5, meanYd, 1E-1);
	}
	
	@Test
	void summarize() {
		
		List<Displacement> displaced = new ArrayList<>(4);
		displaced.add(Displacement.at(0, 0, 1000, 1000, 1010,  990));
		displaced.add(Displacement.at(1, 1, 1000, 9000, 1010, 8990));
		displaced.add(Displacement.at(2, 2, 9000, 9000, 9010, 8990));
		displaced.add(Displacement.at(3, 3, 9000, 1000, 9010,  990));
	
		DisplacementSummary filtered = Displacement.summarize(displaced, d->d.getIndex()<3);
		assertNotNull(filtered);
		assertEquals(3, filtered.sizeX());
		assertEquals(3, filtered.sizeY());
		
		DisplacementSummary unfiltered = Displacement.summarize(displaced);
		assertNotNull(unfiltered);
		assertEquals(4, unfiltered.sizeX());
		assertEquals(4, unfiltered.sizeY());
		
	}
	
	@Test
	void toStringMethod() {
		classUnderTest = Displacement.at(0, 0, 1000, 1000, 1010,  990);
		
		assertEquals("Displacement [type=REG id=0 x=1000.0, y=1000.0, xd=1010.0, yd=990.0, \n" + 
				"\t\tdx=10.0, dy=-10.0]", classUnderTest.toString());
	}

}
