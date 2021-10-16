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
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.ReferencesMatrix;
import net.raumzeitfalle.registration.solver.Deltas;
import net.raumzeitfalle.registration.solver.References;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

/**
 * 
 * Calculates translation (x,y) and scale (x,y) and non-orthogonality (shear, x,y) for the given collection of equations.
 * An affine transform preserves parallelism.
 * 
 * <ul>
 * 	<li>Translation</li>
 *  <li>Rotation</li>
 *  <li>Scale</li>
 *  <li>Shear</li>
 * </ul>
 *  
 * @author oliver
 *
 */
final class BasicAffineModel implements AffineModel {
	
	@Override
	public <T extends Orientable> AffineTransform solve(Collection<AffineModelEquation> equations,
			DegreesOfFreedom degreesOfFreedom) {
		
		// there are 3 coefficients per direction
		int cols = degreesOfFreedom.getDimensions() * 3;
		int rows = equations.size();
		
		ReferencesMatrix references = new ReferencesMatrix(rows, cols);
		DifferencesVector deltas = new DifferencesVector(rows);
		
		Orientation direction = degreesOfFreedom.getDirection();
		prepare(equations, references, deltas, direction);
		
		return solve(references, deltas, direction);
	}

	private AffineTransform solve(References references, Deltas deltas, Orientation direction) {
		
		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		Solution solution = solver.apply(references, deltas);
		
		return createTransform(solution, direction);
	}

	private AffineTransform createTransform(Solution solved, Orientation direction) {
		BasicAffineTransformFactory transformFactory = new BasicAffineTransformFactory(solved);
		return direction.runOperation(transformFactory);
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

		references.set2D(row, eq, direction);
		deltas.set(row, eq);
		
		row++;
		
		return row;
	}

}
