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
import java.util.function.Function;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

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