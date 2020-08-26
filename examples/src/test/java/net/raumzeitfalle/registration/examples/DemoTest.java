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
package net.raumzeitfalle.registration.examples;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class DemoTest {

	@Test
	void runDemos() {
		List<Runnable> examples = new ArrayList<>();
		
		examples.add(()->DemoFourpointsOnlyWithMissingMeas.main());
		examples.add(()->DemoFourpointsScanner.main());
		examples.add(()->DemoFourpointsStandard.main());
		examples.add(()->DemoMultipoint.main());
		examples.add(()->DemoMultipointMagnification.main());
		examples.add(()->DemoMultipointOneDimensional.main());
		examples.add(()->DemoMultipointResidual.main());
		examples.add(()->DemoUnaligned.main());
		
		
		assertDoesNotThrow(()->examples.forEach(Runnable::run));
		
	}
	
}