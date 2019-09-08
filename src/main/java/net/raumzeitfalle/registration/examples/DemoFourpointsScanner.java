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

import net.raumzeitfalle.registration.displacement.Category;
import net.raumzeitfalle.registration.firstorder.Alignments;
import net.raumzeitfalle.registration.firstorder.Compensations;

public class DemoFourpointsScanner {

	public static void main(String ...args) {
		
		Demo demo = new Demo("DEMO: Scanner-like model", "Demo-4Point.csv");
		
		demo.withAlignment(Alignments.SCANNER_SELECTED)
			.selectForAlignment(d->d.belongsTo(Category.ALIGN))
			.selectForCalculation(d->true)
			.selectForRemoval(d->d.belongsTo(Category.INFO_ONLY))
			.withCompensations(Compensations.SCALE, Compensations.ORTHO);
		
		demo.run();
		
	}
	
}
