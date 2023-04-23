package net.raumzeitfalle.jama;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JamaTest {

    @Test
    void that_TestMatrix_returns_no_errors_or_warnings() {
        int errorsAndWarnings = assertDoesNotThrow(()->new TestMatrix().run());
        assertEquals(0, errorsAndWarnings);
    }

    @Test
    void that_MagicSquareExample_runs_without_errors() {
        assertDoesNotThrow(()->new MagicSquareExample().main(null));
    }

}
