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
package net.raumzeitfalle.registration.distortions;

import java.util.Objects;

import net.raumzeitfalle.registration.OrientedOperation;
import net.raumzeitfalle.registration.solver.Solution;

class BasicAffineTransformFactory implements OrientedOperation<AffineTransform> {
	
	private final Solution solution;
	
	public BasicAffineTransformFactory(Solution solved) {
		this.solution = Objects.requireNonNull(solved, "solved must not be null");
	}

	@Override
	public AffineTransform getX() {
		double tx = this.solution.get(2);
		double ox = this.solution.get(1);
		double sx = this.solution.get(0);	
		return SimpleAffineTransform.forX(tx, sx, ox);
	}

	@Override
	public AffineTransform getY() {
		double ty = this.solution.get(2);
		double oy = this.solution.get(1);
		double sy = this.solution.get(0);
		return SimpleAffineTransform.forX(ty, sy, oy);
	}

	@Override
	public AffineTransform getCombined() {
		double sx = this.solution.get(0);
	    double sy = this.solution.get(1);
	    double ox = this.solution.get(2);
	    double oy = this.solution.get(3);
	    double tx = this.solution.get(4);
	    double ty = this.solution.get(5);
		return SimpleAffineTransform.forXY(tx, ty, sx, sy, ox, oy);
	}

}
