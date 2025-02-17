package net.raumzeitfalle.registration.distortions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class HigherOrderEquationTest {

    @Test
    void that_first_quadratic_terms_K7_K8_K9_K10_are_prepared_properly_for_X_Displacement() {
        Displacement d = Displacement.at(1, 1, 800, 900, 802, 898);
        var classUnderTest = HigherOrderEquation.forX(d);
        assertAll(
            // ALL Y related must be 0.0
            ()->assertEquals(0.0, classUnderTest.getK2(), 1E-3, "K2"),
            ()->assertEquals(0.0, classUnderTest.getK4(), 1E-3, "K4"),
            ()->assertEquals(0.0, classUnderTest.getK6(), 1E-3, "K6"),
            ()->assertEquals(0.0, classUnderTest.getK8(), 1E-3, "K8"),
            ()->assertEquals(0.0, classUnderTest.getK10(), 1E-3, "K10"),
            
            // X related
            ()->assertEquals(1.0, classUnderTest.getK1(), 1E-3, "K1"),
            ()->assertEquals(800.0, classUnderTest.getK3(), 1E-3, "K3"),
            ()->assertEquals(900.0, classUnderTest.getK5(), 1E-3, "K5"),
            ()->assertEquals(640000.0, classUnderTest.getK7(), 1E-3, "K7"),
            ()->assertEquals(720000.0, classUnderTest.getK9(), 1E-3, "K9"),
            
            // DELTA
            ()->assertEquals(2.0, classUnderTest.getDeltaValue(), 1E-3, "Delta")
        );
    }

}
