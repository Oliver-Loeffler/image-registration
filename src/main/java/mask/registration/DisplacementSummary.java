package mask.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;

import mask.registration.alignment.AlignmentCalculation;
import mask.registration.alignment.AlignmentTransform;

public class DisplacementSummary {

	public static DisplacementSummary over(Collection<Displacement> t, Predicate<Displacement> calculationSelection) {
		List<Double> dx = new ArrayList<>(t.size());
		List<Double> dy = new ArrayList<>(t.size());

		for (Displacement displacement : t) {
			double deltaX = displacement.dX();
			double deltaY = displacement.dY();

			if (calculationSelection.test(displacement) && Double.isFinite(deltaX)) {
				dx.add(deltaX);
			}

			if (calculationSelection.test(displacement) && Double.isFinite(deltaY)) {
				dy.add(deltaY);
			}
		}
		
		AlignmentTransform alignment = new AlignmentCalculation().apply(t, calculationSelection);

		return new DisplacementSummary(dx, dy, alignment);
	}

	private final DoubleSummaryStatistics statsx;
	
	private final DoubleSummaryStatistics statsy;
	
	private final double sd3x;
	
	private final double sd3y;
	
	private final double rotation;
	
	private DisplacementSummary(List<Double> dx, List<Double> dy, AlignmentTransform alignment) {
		this.statsx = dx.stream().mapToDouble(Double::doubleValue).sorted().summaryStatistics();
		this.statsy = dy.stream().mapToDouble(Double::doubleValue).sorted().summaryStatistics();
		
		this.sd3x = 3* stdev(this.statsx.getAverage(), dx);
		this.sd3y = 3* stdev(this.statsy.getAverage(), dy);
		
		this.rotation = alignment.getRotation();
	}	
	
	public double stdev(double mean, Collection<Double> values) {
		
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
		sb.append("Mean").append(tabs3).append(format(meanX())).append(tabs1).append(format(meanY())).append(System.lineSeparator());
		sb.append("3Sigma").append(tabs3).append(format(sd3X())).append(tabs1).append(format(sd3Y())).append(System.lineSeparator());
		sb.append("Min").append(tabs3).append(format(minX())).append(tabs1).append(format(minY())).append(System.lineSeparator());
		sb.append("Max").append(tabs3).append(format(maxX())).append(tabs1).append(format(maxY())).append(System.lineSeparator());
		sb.append("Rotation").append(tabs3).append(tabs3).append(format(rotation()*1E6)).append(System.lineSeparator());
		sb.append("Sites").append(tabs3).append(this.statsx.getCount()).append(tabs2).append(this.statsy.getCount()).append(System.lineSeparator());
		
		return sb.toString();
	}

	private double rotation() {
		return this.rotation;
	}

	private double meanX() {
		return this.statsx.getAverage();
	}
	
	private double meanY() {
		return this.statsy.getAverage();
	}
	
	private double minX() {
		return this.statsx.getMin();
	}
	
	private double minY() {
		return this.statsy.getMin();
	}
	
	private double maxX() {
		return this.statsx.getMax();
	}
	
	private double maxY() {
		return this.statsy.getMax();
	}
	
	private double sd3X() {
		return this.sd3x;
	}

	private double sd3Y() {
		return this.sd3y;
	}

	private String format(double value) {
		return String.format("%.6f", value*1E3);
	}
	
}
