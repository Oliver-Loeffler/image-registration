/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.DisplacementSummary;

public final class TranslateToCenter implements Function<Collection<Displacement>, List<Displacement>> {

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
