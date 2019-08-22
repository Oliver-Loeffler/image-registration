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
package net.raumzeitfalle.registration;

import java.nio.file.Paths;
import java.util.List;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.SiteSelection;
import net.raumzeitfalle.registration.file.FileLoader;

public class DemoMultipointOneDimensional {

	public static void main(String ...args) {
		
		DemoMultipointOneDimensional demo = new DemoMultipointOneDimensional();
		demo.run();

	}

	private void run() {
		System.out.println(System.lineSeparator() + "--- DEMO: 1-dimensional data ------------------");

		SiteSelection selection = SiteSelection.multipoint();
		List<Displacement> displacements = new FileLoader().load(Paths.get("DemoOneDimensionXonly.csv"));
		
		/* TODO: Define flow how to process 1D data
		 * Options are:
		 * 	- separate models for X,Y, then only apply what is needed ==> what about rotation?
		 *  - model considers X and Y, then what to do if either X or Y are not given? ==> populate with 0.0 upfront?
		 * 
		 *   
		 *   
		 * Open:
		 *  - Test & implement handling of 1D cases
		 */
	}

	
}
