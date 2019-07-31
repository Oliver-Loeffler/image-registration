package mask.registration.alignment;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.Displacement;

public class AlignmentEquation {
	
	public static Stream<AlignmentEquation> from(Displacement d) {
		
		Builder<AlignmentEquation> builder = Stream.builder();
		
		if (Double.isFinite(d.getXd())) {
			builder.accept(AlignmentEquation.forX(d));
		}
		
		if (Double.isFinite(d.getYd())) {
			builder.accept(AlignmentEquation.forY(d));
		}
		
		return builder.build();
	}
	
	public static AlignmentEquation forX(Displacement d) {
		
		double xf = 1.0;
		double yf = 0.0;
		
		double design = -d.getY();
		double delta = d.dX();
		
		return new AlignmentEquation(xf, yf, design, delta);
	}
	
	public static AlignmentEquation forY(Displacement d) {
		
		double xf = 0.0;
		double yf = 1.0;
		
		double design = d.getX();
		double delta = d.dY();
		
		return new AlignmentEquation(xf, yf, design, delta);
	}
	
	private final double xf;
	
	private final double yf;
	
	private final double designValue;
	
	private final double deltaValue;
	
	private AlignmentEquation(double xf, double yf, double design, double delta) {
		this.xf = xf;
		this.yf = yf;
		this.designValue = design;
		this.deltaValue = delta;
	}

	@Override
	public String toString() {
		return "AlignmentEquation [xf=" + xf + ", yf=" + yf + ", designValue=" + designValue + ", deltaValue="
				+ deltaValue + "]";
	}

	public double getXf() {
		return xf;
	}

	public double getYf() {
		return yf;
	}

	public double getDesignValue() {
		return designValue;
	}

	public double getDeltaValue() {
		return deltaValue;
	}
	
	
}
