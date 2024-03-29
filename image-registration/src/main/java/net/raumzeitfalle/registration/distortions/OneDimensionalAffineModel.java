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

import java.util.Collection;
import java.util.Iterator;

import net.raumzeitfalle.registration.DegreesOfFreedom;
import net.raumzeitfalle.registration.DifferencesVector;
import net.raumzeitfalle.registration.Distribution;
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.ReferencesMatrix;
import net.raumzeitfalle.registration.solver.Deltas;
import net.raumzeitfalle.registration.solver.References;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

class OneDimensionalAffineModel implements AffineModel {
	
	OneDimensionalAffineModel() {
		
	}

	@Override
	public <T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations,
			DegreesOfFreedom degreesOfFreedom) {
		
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
		
		ReferencesMatrix references = new ReferencesMatrix(rows, cols);
		DifferencesVector deltas = new DifferencesVector(rows);

		Orientation direction = degreesOfFreedom.getDirection();

		prepare(equations, references, deltas, direction);
		
		return solve(references, deltas, degreesOfFreedom);
	}

	private void prepare(Collection<AffineModelEquation> equations, ReferencesMatrix references, DifferencesVector deltas,
			Orientation direction) {
		int row = 0;
		Iterator<AffineModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(ReferencesMatrix references, DifferencesVector deltas, int row, AffineModelEquation eq,
			Orientation direction) {
		
		references.set1D(row, eq, direction);
		deltas.set(row, eq);
		
		row++;
		return row;
		
	}
	
	private AffineTransform solve(References references, Deltas deltas, DegreesOfFreedom dof) {
		
		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		Solution solution = solver.apply(references, deltas);
		
		return createTransform(solution, dof);
	}

	private AffineTransform createTransform(Solution solved, DegreesOfFreedom dof) {
		
		double scale = solved.get(0);
	    double rot = solved.get(1);
	    double tx = solved.get(2);
	    double ty = solved.get(3);
	    
		if (Distribution.HORIZONTAL.equals(dof.getDistribution())) {
			return SimpleAffineTransform.horizontal(tx, ty, scale, rot);
		}
		
		if (Distribution.VERTICAL.equals(dof.getDistribution())) {
			return SimpleAffineTransform.vertical(tx, ty, scale, rot);
		}
		
		String message = String.format("Evaluation of data with %s spatial orientation not supported.", dof.getDistribution());
		throw new UnsupportedOperationException(message);
	}

}
