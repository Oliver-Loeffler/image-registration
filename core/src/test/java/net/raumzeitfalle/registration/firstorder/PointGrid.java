/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019, 2021 Oliver Loeffler, Raumzeitfalle.net
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
package net.raumzeitfalle.registration.firstorder;

import java.util.*;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public class PointGrid {
	
	public static PointGrid withNodes(int nodes) {
		return new PointGrid().ofNodes(nodes);
	}
	
	private int nodesPerDirection = 5;
	
	private double width = 140000d;
	
	private double height = 140000d;
	
	public PointGrid ofNodes(int nodes) {
		this.nodesPerDirection = nodes;
		return this;
	}

	public List<Displacement> create() {
		
		double pitchx = width / (nodesPerDirection-1);
		double pitchy = height / (nodesPerDirection-1);
		
		double posx = - width / 2;
		double posy = - height / 2;
		
		int index = 1;
		List<Displacement> displacements = new ArrayList<>(nodesPerDirection*nodesPerDirection);
		for (int x = 0; x < nodesPerDirection; x++) {
			for (int y = 0; y < nodesPerDirection; y++) {
				displacements.add(Displacement.at(index, index, posx + x*pitchx, 
						posy + y*pitchy, 
					    posx + x*pitchx,
					    posy + y*pitchy));
			}
		}
		
		return displacements;
	}
	
	public List<Displacement> moveBy(double shiftx, double shifty) {
		return create().stream()
					   .map(d->d.correctBy(shiftx, shifty))
					   .collect(Collectors.toList());
	}
	
	
}
