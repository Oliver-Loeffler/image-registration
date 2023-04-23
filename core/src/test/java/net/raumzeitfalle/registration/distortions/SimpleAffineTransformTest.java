package net.raumzeitfalle.registration.distortions;

import net.raumzeitfalle.registration.displacement.Displacement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleAffineTransformTest {


    @ParameterizedTest
    @CsvSource({
           "0, 70000, 20000, 70000.123, 19999.991",
           "0,   NaN, 20000, 70000.123, 19999.991",
           "0, 70000,   NaN, 70000.123, 19999.991",
           "0,   NaN,   NaN, 70000.123, 19999.991",
    })
    void that_NaN_design_values_will_not_lead_to_NaN_results(int site, double designX, double designY, double dx, double dy) {
        AffineTransform transform = SimpleAffineTransform.forXY(1, -1, 0.0,0.0, 0.0,0.0);
        Displacement source = Displacement.at(site, site, designX, designY, dx, dy);
        Displacement result = transform.apply(source);

        assertAll(()->assertTrue(Double.isFinite(result.getXd())),
                  ()->assertTrue(Double.isFinite(result.getYd())));
    }
}
