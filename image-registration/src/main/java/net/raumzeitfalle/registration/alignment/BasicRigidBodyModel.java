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
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.Iterator;

import net.raumzeitfalle.registration.DegreesOfFreedom;
import net.raumzeitfalle.registration.DifferencesVector;
import net.raumzeitfalle.registration.Orientable;
import net.raumzeitfalle.registration.Orientation;
import net.raumzeitfalle.registration.ReferencesMatrix;
import net.raumzeitfalle.registration.solver.Solution;
import net.raumzeitfalle.registration.solver.SolverProvider;
import net.raumzeitfalle.registration.solver.spi.SolverAdapter;

/**
 * 
 * Calculates translation and rotation for the given set of equations. A rigid
 * transform preserves all distances between points.
 *
 * <ul>
 * <li>Translation</li>
 * <li>Rotation</li>
 * </ul>
 * 
 */
final class BasicRigidBodyModel implements RigidBodyModel {

	private void prepare(Collection<RigidModelEquation> equations, ReferencesMatrix references, DifferencesVector deltas, Orientation direction) {
		int row = 0;
		Iterator<RigidModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(ReferencesMatrix references, DifferencesVector deltas, int row, RigidModelEquation eq, Orientation direction) {

		references.set(row, eq, direction);
		deltas.set(row, eq);

		row++;
		return row;
	}

	private RigidTransform solve(ReferencesMatrix references, DifferencesVector deltas, Orientation direction) {

		SolverAdapter solver = SolverProvider.getInstance().getSolver();
		Solution solution = solver.apply(references, deltas);
		
		return createTransform(solution,direction);
	}

	private RigidTransform createTransform(Solution solved, Orientation direction) {
		RigidTransformFactory transformFactory = new RigidTransformFactory(solved);
		return direction.runOperation(transformFactory);
	}

	@Override
	public <T extends Orientable> RigidTransform solve(Collection<RigidModelEquation> equations, DegreesOfFreedom dof) {
		
		Orientation ori = dof.getDirection();
		int rows = equations.size();
		int cols = ori.getDimensions()+1;
				
		DifferencesVector deltas = new DifferencesVector(rows);
		ReferencesMatrix references = new ReferencesMatrix(rows, cols);
		prepare(equations, references, deltas, ori);
		
		// escape here before singular matrix exception can be thrown
		if (1 == dof.getCombined()) {
			return ori.runOperation(new RigidTransformTranslationsFactory(deltas));
		}				
		return solve(references, deltas, ori);
	}
	
}
