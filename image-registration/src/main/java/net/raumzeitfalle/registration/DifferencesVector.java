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
package net.raumzeitfalle.registration;

import net.raumzeitfalle.registration.solver.Deltas;

public final class DifferencesVector implements Deltas {

	private final double[] deltas;
	
	public DifferencesVector(int rows) {
		this.deltas = new double[rows];
	}

	public final double[] getArray() {
		return this.deltas;
	}

	protected final void set(int row, double deltaValue) {
		this.deltas[row] = deltaValue;
	}
	
	public final void set(int row, ModelEquation eq) {
		set(row, eq.getDeltaValue());
	}

	public final double get(int row) {
		return this.deltas[row];
	}

	public final int rows() {
		return this.deltas.length;
	}
	
}
