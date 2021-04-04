package net.raumzeitfalle.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DoubleStatisticsSummaryTest {

	@Test
	void sdOfSample() {
		
		DoubleStatisticsSummary summary = new DoubleStatisticsSummary();
		
		summary.accept(1.0);
		summary.accept(2.0);
		summary.accept(3.0);
		summary.accept(4.0);
		summary.accept(5.0);
		summary.accept(6.0);
				
		assertEquals(1.87083, summary.getStdDevOfSample(), 1E-5);
		
	}

}
