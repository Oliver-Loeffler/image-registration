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

import java.util.*;

import net.raumzeitfalle.registration.*;
import net.raumzeitfalle.registration.solver.*;

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
 * @author oliver
 * 
 */
final class DefaultRigidBodyModel implements RigidBodyModel {

	private void prepare(Collection<RigidModelEquation> equations, References references, Deltas deltas, Orientation direction) {
		int row = 0;
		Iterator<RigidModelEquation> it = equations.iterator();
		while (it.hasNext()) {
			row = addEquation(references, deltas, row, it.next(), direction);
		}
	}

	private int addEquation(References references, Deltas deltas, int row, RigidModelEquation eq, Orientation direction) {

		references.set(row, eq, direction);
		deltas.set(row, eq);

		row++;
		return row;
	}

	private RigidTransform solve(References references, Deltas deltas, Orientation direction) {

		Solver solver = new SolverProvider().getSolver();
		Solution solution = solver.apply(references, deltas);
		
		return createTransform(solution,direction);
	}

	private RigidTransform createTransform(Solution solved, Orientation direction) {
		
		if (Orientation.X.equals(direction)) {
			return SimpleRigidTransform
					.with(solved.get(0), 0.0, solved.get(1));
		}
		
		if (Orientation.Y.equals(direction)) {
			return SimpleRigidTransform
					.with(0.0, solved.get(0), solved.get(1));
		}
		
		return SimpleRigidTransform
				.with(solved.get(0), solved.get(1), solved.get(2));
	}

	@Override
	public <T extends Orientable> RigidTransform solve(Collection<RigidModelEquation> equations, Dimension<T> dimension) {
		
		int rows = equations.size();
		int cols = dimension.getDimensions()+1;
		
		Orientation direction = dimension.getDirection();
		
		Deltas deltas = new Deltas(rows);
		References references = new References(rows, cols);
		prepare(equations, references, deltas, direction);
		
		// escape here before singular matrix exception can be thrown
		if (Orientation.XY.equals(direction) && equations.size() == 2) {
			double tx = deltas.get(0);
			double ty = deltas.get(1);
			return SimpleRigidTransform.translation(tx, ty);
		}
		
		if (Orientation.X.equals(direction) && equations.size() == 1) {
			double tx = deltas.get(0);
			return SimpleRigidTransform.shiftX(tx);
		}
		
		if (Orientation.Y.equals(direction) && equations.size() == 1) {
			double ty = deltas.get(0);
			return SimpleRigidTransform.shiftY(ty);
		}
				
		return solve(references, deltas, direction);
	}
	
}
