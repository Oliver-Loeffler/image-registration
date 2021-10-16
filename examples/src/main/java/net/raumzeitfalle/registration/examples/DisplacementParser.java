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
package net.raumzeitfalle.registration.examples;

import java.util.function.*;

import net.raumzeitfalle.registration.displacement.*;

class DisplacementParser implements BiFunction<Integer,String,Displacement>{

	@Override
	public Displacement apply(Integer index, String line) {
		String[] e = line.split(",");
		
		double x = Double.parseDouble(e[0]);
		double xd = Double.parseDouble(e[2]);
		double y = Double.parseDouble(e[1]);
		double yd = Double.parseDouble(e[3]);
		
		String type = e[6];
		int firstQuote = type.indexOf("\"");
		int lastQuote = type.lastIndexOf("\"");
		
		Category category = Category.REG;
		if (firstQuote >= 0 && lastQuote >= 0) {
			String siteClass = type.substring(firstQuote+1, lastQuote);
			category = Category.fromString(siteClass);
		} 				
		
		return Displacement.at(index,index, x, y, xd, yd, category);
	}

}
