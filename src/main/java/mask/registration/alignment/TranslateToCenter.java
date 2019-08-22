package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;

import mask.registration.displacement.Displacement;
import mask.registration.displacement.DisplacementSummary;

public class TranslateToCenter implements Function<Collection<Displacement>, List<Displacement>> {

	private OptionalDouble meanx = OptionalDouble.empty();
	
	private OptionalDouble meany = OptionalDouble.empty();
	
	public Translate reverse() {
		return new Translate(meanx.orElse(0.0), meany.orElse(0.0));
	}
	
	@Override
	public List<Displacement> apply(Collection<Displacement> t) {
		
		DisplacementSummary summary = Displacement.summarize(t, d->true);
		double mx = summary.designMeanX();
		double my = summary.designMeanY();
		
		List<Displacement> centered = t.stream()
									   .map(displacement -> displacement.moveBy(-mx, -my))
									   .collect(Collectors.toList());
		
		this.meanx = OptionalDouble.of(mx);
		this.meany = OptionalDouble.of(my);
		
		return centered;
	}

	public OptionalDouble getMeanx() {
		return meanx;
	}

	public OptionalDouble getMeany() {
		return meany;
	}

}
