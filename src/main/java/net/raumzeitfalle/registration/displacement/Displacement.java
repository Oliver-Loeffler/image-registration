package net.raumzeitfalle.registration.displacement;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * The {@link Displacement} class is a pure data class. Its purpose is to bind all details together, which are needed to calculate image registration for a single location.
 * <p>
 * Each displacement is bound to a specific location on a mask (x,y).<br>
 * The measured position of the structure is specified by (x<sub>d</sub>, y<sub>d</sub>).<br>
 * The differences between (x,y)<sub>d</sub> and (x,y) are calculated when a {@link Displacement} object is created.<br>
 * <p>
 * Depending of the location on a mask, a geometrical feature has a specific task. To distinguish between different features, the {@link SiteClass} enum category type is used. 
 * 
 * @author oliver
 *
 */
public class Displacement {

	/**
	 * Creates a new {@link Displacement} object based on a given source object.
	 * The actual displacement values (x<sub>d</sub>, y<sub>d</sub>) will be updated accordingly.
	 * 
	 * @param source {@link Displacement}
	 * @param xd - new displacement value for X-axis (design plus deviation)
	 * @param yd - new displacement value for Y-axis (design plus deviation)
	 * 
	 * @return {@link Displacement}
	 */
	public static Displacement from(Displacement source, double xd, double yd) {
		return new Displacement(source.index,source.id, source.x, source.y, xd, yd, source.siteClass);
	}
	
	/**
	 * Creates a new {@link Displacement} object from scratch.
	 * 
	 * @param index Usually a consecutive number (e.g. from a table) reflecting the order of {@link Displacement} object creation.
	 * @param id An arbitrary ID value which can be assigned.
	 * @param x Actual design location (X-direction) for a displacement result.
	 * @param y Actual design location (Y-direction) for a displacement result.
	 * @param type Category type to distinguish different {@link Displacement} groups.
	 * 
	 * @return {@link Displacement}
	 */
	public static Displacement at(int index, int id, double x, double y, double xd, double yd, SiteClass type) {
		return new Displacement(index,id, x, y, xd, yd, type);
	}
	
	/**
	 * Creates a statistical summary for the given collection of {@link Displacement} instances.
	 * For both axes (X,Y) descriptive statistics such as average (mean), min, max, 3Sigma etc. are provided.
	 * 
	 * @param t
	 * @param calculationSelection
	 * @return
	 */
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
	
	private final int index;
	
	private final int id;
	
	private final double x;
	private final double y;
	private final double xd;
	private final double yd;
	
	private final double dx;
	private final double dy;
	
	private final SiteClass siteClass;
	
	private Displacement(int index, int id, double x, double y, double xd, double yd, SiteClass type) {
		this.index = index;
		this.id = id;
		
		this.x = x;
		this.y = y;
		this.xd = xd;
		this.yd = yd;
		
		this.dx = xd -x;
		this.dy = yd -y;
		
		this.siteClass = Objects.requireNonNull(type, "type must not be null");
	}
	
	public Displacement moveBy(double offsetX, double offsetY) {
		return new Displacement(index, id, offsetX + x, offsetY + y, offsetX + xd, offsetY + yd, siteClass);
	}
	
	public Displacement correctBy(double dx, double dy) {
		return new Displacement(index, id, x, y, xd - dx, yd - dy, siteClass);
	}

	public int getIndex() {
		return index;
	}
	
	public int getId() {
		return id;
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
		return "Displacement [type="+siteClass.name()+ " id=" + id + " x=" + x + ", y=" + y + ", xd=" + xd + ", yd=" + yd + ", "
				+ System.lineSeparator() + "\t\tdx=" + dx + ", dy=" + dy + "]";
	}

	public boolean isOfType(SiteClass other) {
		return this.siteClass.equals(other);
	}
	
	

}
