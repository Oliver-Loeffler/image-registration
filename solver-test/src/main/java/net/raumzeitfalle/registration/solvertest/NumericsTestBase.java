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
package net.raumzeitfalle.registration.solvertest;

import java.util.*;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.displacement.*;
import net.raumzeitfalle.registration.distortions.AffineTransform;

public abstract class NumericsTestBase {

	private final NumericsTestRunner runner = new NumericsTestRunner();

	public void run(List<Displacement> source) {
		this.runner.accept(source);
	}
	
	public NumericsTestRunner getRunner() {
		return this.runner;
	}
	
	public RigidTransform getCorrectedAlignment() {
		return this.runner.getCorrectedAlignment();
	}
	
	public RigidTransform getUncorrectedAlignment() {
		return this.runner.getUncorrectedAlignment();
	}
	
	public AffineTransform getUncorrectedFirstOrder() {
		return this.runner.getUncorrectedFirstOrder();
	}

	public AffineTransform getFirstOrder() {
		return this.runner.getFirstOrder();
	}
	
	public AffineTransform getCorrectedFirstOrder() {
		return this.runner.getCorrectedFirstOrder();
	}
	
	public List<Displacement> listOf(Displacement... d) {
		return Arrays.asList(d);
	}

}
