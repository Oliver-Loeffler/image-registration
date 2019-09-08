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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.alignment.RigidTransformCalculation;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.CenteredAffineTransformCalculation;
import net.raumzeitfalle.util.DoubleStatisticsSummary;


/**
 * Creates summary for any collection of displacements showing min/max/mean/3sigma values of deviations from reference for (x,y)-Directions. Also first order (scaling, non-orthogonality) and alignment (translation, rotation) details are shown.
 * 
 * @author Oliver Loeffler
 *
 */
public final class DisplacementSummary {

	/**
	 * Generates a summary table for (x,y)-Directions showing min/max/mean/3sigma values for deviations from reference. Also first order and alignment details are shown.
	 * @param displacements Collection of displacements
	 * @param calculationSelection Use {@link SiteSelection} to define which site shall be used for positional calculation, alignment and first order calculation.
	 * @return Summary table
	 */
	public static DisplacementSummary over(Collection<Displacement> displacements, Predicate<Displacement> calculationSelection) {
		return new DisplacementSummary(displacements, calculationSelection);
	}

	private final DoubleStatisticsSummary statsDiffX = new DoubleStatisticsSummary();
	
	private final DoubleStatisticsSummary statsDiffY = new DoubleStatisticsSummary();
	
	private final DoubleStatisticsSummary statsRefX = new DoubleStatisticsSummary();
	
	private final DoubleStatisticsSummary statsRefY = new DoubleStatisticsSummary();
	
	private final DoubleStatisticsSummary statsPosX = new DoubleStatisticsSummary();
	
	private final DoubleStatisticsSummary statsPosY = new DoubleStatisticsSummary();
	
	private final double sd3x;
	
	private final double sd3y;
	
	private final double rotation;
	
	private final double scalex;
	
	private final double scaley;
	
	private final double magnification;
	
	private final double orthox;
	
	private final double orthoy;
	
	private final double ortho;
	
	private DisplacementSummary(Collection<Displacement> displacements, Predicate<Displacement> calculationSelection) {
		
		List<Double> dx = new ArrayList<>(displacements.size());
		List<Double> dy = new ArrayList<>(displacements.size());
		
		displacements.stream()
					 .filter(calculationSelection)
					 .forEach(d -> {
						 if (Double.isFinite(d.dX())) { dx.add(d.dX()); }
						 if (Double.isFinite(d.dY())) { dy.add(d.dY()); }
						 update(d);
					 });
		
		if (Double.isFinite(this.statsDiffX.getAverage())) {
			this.sd3x = 3 * stdev(this.statsDiffX.getAverage(), dx);	
		} else {
			this.sd3x = Double.NaN;	
		}
		
		if (Double.isFinite(this.statsDiffY.getAverage())) {
			this.sd3y = 3* stdev(this.statsDiffY.getAverage(), dy);	
		} else {
			this.sd3y = Double.NaN;	
		}

		RigidTransform alignment = new RigidTransformCalculation()
				.apply(displacements, calculationSelection);
		
		AffineTransform firstOrder = new CenteredAffineTransformCalculation()
				.apply(displacements, calculationSelection);
				
		
		this.rotation = alignment.getRotation();		
		this.scalex = firstOrder.getScaleX();
		this.scaley = firstOrder.getScaleY();
		this.magnification = firstOrder.getMagnification();
		
		this.orthox = firstOrder.getOrthoX();
		this.orthoy = firstOrder.getOrthoY();
		this.ortho =  firstOrder.getOrtho();
	}	
	
	private void update(Displacement d) {
		acceptFinites(d.dX(), statsDiffX::accept);
		acceptFinites(d.dY(), statsDiffY::accept);
		
		acceptFinites(d.getX(), statsRefX::accept);
		acceptFinites(d.getY(), statsRefY::accept);
		
		acceptFinites(d.getXd(), statsPosX::accept);
		acceptFinites(d.getYd(), statsPosY::accept);
	}

	private void acceptFinites(double value, DoubleConsumer consumer) {
		if (Double.isFinite(value)) {
			consumer.accept(value);
		}
	}
	
	private double stdev(double mean, Collection<Double> values) {
		
		if (values.isEmpty()) {
			return Double.NaN;
		}

		double sum = values.stream()
					       .map(d -> Double.valueOf(Math.pow(d.doubleValue() - mean, 2)))
					       .mapToDouble(Double::doubleValue)
					       .sum();

		return Math.sqrt(sum / (values.size() - 1));
	}

	@Override
	public String toString() {
		String tabs3 = "\t\t\t";
		String tabs2 = "\t\t";
		String tabs1 = "\t";
		StringBuilder sb = new StringBuilder(System.lineSeparator())
				.append("DisplacementSummary")
				.append(System.lineSeparator());
		
		sb.append("Summary").append(tabs3).append("X").append(tabs2).append("Y").append(tabs2).append("ISO").append(System.lineSeparator());
		sb.append("Mean").append(tabs2).append(format(meanX())).append(tabs1).append(format(meanY())).append(System.lineSeparator());
		sb.append("3Sigma").append(tabs2).append(format(sd3X())).append(tabs1).append(format(sd3Y())).append(System.lineSeparator());
		sb.append("Min").append(tabs2).append(format(minX())).append(tabs1).append(format(minY())).append(System.lineSeparator());
		sb.append("Max").append(tabs2).append(format(maxX())).append(tabs1).append(format(maxY())).append(System.lineSeparator());
		sb.append("Scales:").append(tabs2).append(format(scalex*1E6)).append(tabs1).append(format(scaley*1E6)).append(tabs1).append(format(magnification*1E6)).append(System.lineSeparator());
		sb.append("Orthos:").append(tabs2).append(format(orthox*1E6)).append(tabs1).append(format(orthoy*1E6)).append(tabs1).append(format(ortho*1E6)).append(System.lineSeparator());
		sb.append("Rotation:").append(tabs2).append(tabs3).append(format(rotation()*1E6)).append(System.lineSeparator());
		sb.append("Sites").append(tabs2).append(this.statsDiffX.getCount()).append(tabs2).append(this.statsDiffY.getCount()).append(System.lineSeparator());
		return sb.toString();
	}

	private double rotation() {
		return this.rotation;
	}

	public double meanX() {
		return this.statsDiffX.getAverage();
	}
	
	public double meanY() {
		return this.statsDiffY.getAverage();
	}
	
	public double designMeanX() {
		return this.statsRefX.getAverage();
	}
	
	public double designMeanY() {
		return this.statsRefY.getAverage();
	}
	
	public double designMaxX() {
		return this.statsRefX.getMax();
	}
	
	public double designMaxY() {
		return this.statsRefY.getMax();
	}
	
	public double designMinX() {
		return this.statsRefX.getMin();
	}
	
	public double designMinY() {
		return this.statsRefY.getMin();
	}
	
	public double displacedMeanX() {
		return this.statsPosX.getAverage();
	}
	
	public double displacedMeanY() {
		return this.statsPosY.getAverage();
	}
	
	public double minX() {
		return this.statsDiffX.getMin();
	}
	
	public double minY() {
		return this.statsDiffY.getMin();
	}
	
	public double maxX() {
		return this.statsDiffX.getMax();
	}
	
	public double maxY() {
		return this.statsDiffY.getMax();
	}
	
	public double sd3X() {
		return this.sd3x;
	}

	public double sd3Y() {
		return this.sd3y;
	}
	
	public long sizeX() {
		return this.statsDiffX.getCount();
	}

	public long sizeY() {
		return this.statsDiffY.getCount();
	}

	private String format(double value) {
		return String.format("%10.5f", value*1E3);
	}
	
}
