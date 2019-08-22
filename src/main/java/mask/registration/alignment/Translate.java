package mask.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import mask.registration.displacement.Displacement;

public class Translate implements Function<Collection<Displacement>, List<Displacement>> {
	
	private final double x;
	
	private final double y;
	
	public Translate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public List<Displacement> apply(Collection<Displacement> t) {
		return t.stream()
				   .map(displacement -> displacement.moveBy(x,y))
				   .collect(Collectors.toList());

	}
	
}
