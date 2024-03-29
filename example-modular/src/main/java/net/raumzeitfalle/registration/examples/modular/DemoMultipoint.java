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
package net.raumzeitfalle.registration.examples.modular;

import net.raumzeitfalle.registration.firstorder.Alignments;

public class DemoMultipoint {

	public static void main(String ...args) {
		
		Demo demo = new Demo("DEMO: Multipoint", "Demo-4Point.csv");
		
		demo.withAlignment(Alignments.ALL)
			.selectForAlignment(d->true)
			.selectForCalculation(d->true)
			.selectForRemoval(d->false);
		
		demo.run();

	}
	
}
