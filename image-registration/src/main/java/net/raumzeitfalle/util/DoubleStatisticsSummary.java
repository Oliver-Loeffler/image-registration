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
package net.raumzeitfalle.util;

import java.util.DoubleSummaryStatistics;

public final class DoubleStatisticsSummary {
	
	private float loadFactor = 0.5f;
	
	private double[] values;
	
	private int cursor;
	
	private final DoubleSummaryStatistics stats;
	
	public DoubleStatisticsSummary() {
		this(2_000);
	}
	
	protected DoubleStatisticsSummary(int initialCapacity) {
		this.values = new double[initialCapacity];
		this.stats = new DoubleSummaryStatistics();
	}
	
	public final void accept(double value) {
		if (!Double.isFinite(value))
			return;
		stats.accept(value);
		memorize(value);
	}
	
	private final void memorize(double value) {
		increaseArraySize();
		values[cursor++] = value;
	}

	private final void increaseArraySize() {
		if (cursor >= values.length) {
			int expansion = 1 + (int)(values.length*loadFactor);
			double[] moreValues = new double[values.length + expansion];
			System.arraycopy(values, 0, moreValues, 0, values.length);
			values = moreValues;
			System.out.println("Exp: " + values.length);
		}
	}

	public final double getRange() {
		return stats.getMax() - stats.getMin();
	}
	
	public final double getStdDevOfSample() {
		if (stats.getCount() == 0)
			return Double.NaN;
		
		final double sumOfSquares = sumOfSquares();
		return Math.sqrt(sumOfSquares / (cursor - 1));
	}

	private double sumOfSquares() {
		final DoubleSummaryStatistics sumOfSquares = new DoubleSummaryStatistics();
		final double mean = stats.getAverage();
		double dev;
		for (int i = 0; i < cursor; i++ ) {
			dev = values[i]-mean;
			sumOfSquares.accept(dev*dev);
		}
		return sumOfSquares.getSum();
	}
	
	public final double getAverage() {
		return stats.getAverage();
	}
	
	public final long getCount() {
		return stats.getCount();
	}
	
	public final double getMin() {
		return stats.getMin();
	}
	
	public final double getMax() {
		return stats.getMax();
	}

}
