package mask.registration;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mask.registration.alignment.AlignmentEquation;

public class Displacement {

	public static Displacement of(double x, double y, double xd, double yd, SiteClass type) {
		return new Displacement(x, y, xd, yd, type);
	}
	
	public static double average(Collection<Displacement> t, ToDoubleFunction<Displacement> mapper) {
		return average(t, d->true, mapper);
	}
	
	public static DisplacementSummary summarize(Collection<Displacement> t, Predicate<Displacement> calculationSelection) {
		return DisplacementSummary.over(t, calculationSelection);
	}
	
	public static double average(Collection<Displacement> t, Predicate<Displacement> filter, ToDoubleFunction<Displacement> mapper) {
		return t.stream()
				.filter(filter)
				.mapToDouble(mapper)
				.filter(Double::isFinite)
				.average()
				.orElse(Double.NaN);
	}
	
	private final double x;
	private final double y;
	private final double xd;
	private final double yd;
	
	private final double dx;
	private final double dy;
	
	private final SiteClass siteClass;
	
	private Displacement(double x, double y, double xd, double yd, SiteClass type) {
		this.x = x;
		this.y = y;
		this.xd = xd;
		this.yd = yd;
		
		this.dx = xd -x;
		this.dy = yd -y;
		
		this.siteClass = Objects.requireNonNull(type, "type must not be null");
	}
	
	public Displacement moveBy(double offsetX, double offsetY) {
		return new Displacement(offsetX + x, offsetY + y, offsetX + xd, offsetY + yd, siteClass);
	}
	
	public Displacement correctBy(double dx, double dy) {
		return new Displacement(x, y, xd - dx, yd - dy, siteClass);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getXd() {
		return xd;
	}

	public double getYd() {
		return yd;
	}
	
	public double dX() {
		return dx;
	}

	public double dY() {
		return dy;
	}

	public SiteClass getType() {
		return siteClass;
	}

	@Override
	public String toString() {
		return "Displacement [type="+siteClass.name()+ " x=" + x + ", y=" + y + ", xd=" + xd + ", yd=" + yd + ", dx=" + dx + ", dy=" + dy + "]";
	}

	public boolean isOfType(SiteClass other) {
		return this.siteClass.equals(other);
	}
	
	

}
