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
package net.raumzeitfalle.registration;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import net.raumzeitfalle.registration.displacement.Displacement;

public class SpatialDistribution implements Consumer<Displacement>, UnaryOperator<Displacement>{

	private final Set<Double> xLocations = new HashSet<>(1000);
	
	private final Set<Double> yLocations = new HashSet<>(1000);
	
	@Override
	public void accept(Displacement t) {

		/* 
		 * 
		 * Displacement with value Double.NaN will not get converted
		 * into any type of equation. Therefore only finite displacements
		 * are considered to analyse spatial coordinate distribution.
		 * 
		 */
		if (Double.isFinite(t.getXd())) 
			xLocations.add(Double.valueOf(t.getX()));
		
		if (Double.isFinite(t.getYd())) 
			yLocations.add(Double.valueOf(t.getY()));
		
	}
	
	@Override
	public Displacement apply(Displacement t) {
		this.accept(t);
		return t;
	}

	@Override
	public String toString() {
		return String.format("Distribution [xLocations=%s, yLocations=%s]", xLocations.size(), yLocations.size());
	}

	public Distribution getDistribution() {
		if (xLocations.isEmpty() && yLocations.isEmpty()) {
			throw new IllegalArgumentException("Could not determine data distribution as no valid displacements have been processed yet.");	
		}
		
		if (xLocations.size() > 1  && yLocations.size() > 1)
			return Distribution.AREA;
		
		if (xLocations.size() == 1 && yLocations.size() > 1)
			return Distribution.VERTICAL;
		
		if (yLocations.size() == 1 && xLocations.size() > 1)
			return Distribution.HORIZONTAL;
		
		return Distribution.SINGULARITY;
	}
	
	

}
