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
package net.raumzeitfalle.registration.distortions;

import java.util.*;

import net.raumzeitfalle.registration.*;
import net.raumzeitfalle.registration.core.*;
import net.raumzeitfalle.registration.solver.*;

class DefaultOneDimensionalAffineModel implements AffineModel {
	
	private final Distribution spatialOrientation;
	
	DefaultOneDimensionalAffineModel(SpatialDistribution distribution) {
		this.spatialOrientation = distribution.getDistribution();
	}

	@Override
	public <T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations,
			Dimension<T> dimension) {
		
		/*
		 * Assumption:
		 *  all data points distributed along one vertical line
		 *  different Y locations, exactly one X location
		 *  => this would render the normal affine model to fail 
		 *     with a singular matrix  
		 * 
		 *  What can be calculated?
		 *   1. translation-x
		 *   2. translation-y
		 *   3. scale-y
		 *   4. ortho-y (aka. rotation in this case)
		 *  
		 *  What can't be calcutaled? 
		 *   1. scale-x (this would require multiple design x-locations)
		 *   
		 *  Same applies in case of data distributed along one horizontal
		 *  line.
		 * 
		 */
		
		int cols = 4; // dimension.getDimensions() * 2;  
		int rows = equations.size();
		
		ReferencesImpl references = new ReferencesImpl(rows, cols);
		DeltasImpl deltas = new DeltasImpl(rows);

		Orientation direction = dimension.getDirection();

		prepare(equations, references, deltas, direction);
		
		return solve(references, deltas, direction);
	}

	private void prepare(Collection<AffineModelEquation> equations, ReferencesImpl references, DeltasImpl deltas,
			Orientation direction) {
		int row = 0;
		Iterator<AffineModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(ReferencesImpl references, DeltasImpl deltas, int row, AffineModelEquation eq,
			Orientation direction) {
		
		references.set1D(row, eq, direction);
		deltas.set(row, eq);
		
		row++;
		return row;
		
	}
	
	private AffineTransform solve(References references, Deltas deltas, Orientation direction) {
		
		Solver solver = new SolverProvider().getSolver();
		Solution solution = solver.apply(references, deltas);
		
		return createTransform(solution, direction);
	}

	private AffineTransform createTransform(Solution solved, Orientation direction) {
		
		double scale = solved.get(0);
	    double rot = solved.get(1);
	    double tx = solved.get(2);
	    double ty = solved.get(3);
	    
		if (Distribution.HORIZONTAL.equals(spatialOrientation)) {
			return SimpleAffineTransform.horizontal(tx, ty, scale, rot);
		}
		
		if (Distribution.VERTICAL.equals(spatialOrientation)) {
			return SimpleAffineTransform.vertical(tx, ty, scale, rot);
		}
		
		String message = String.format("Evaluation of data with %s spatial orientation not supported.", spatialOrientation);
		throw new UnsupportedOperationException(message);
	}

}
