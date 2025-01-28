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
package net.raumzeitfalle.registration.displacement;

import java.util.function.BiFunction;

/**
 * Minimalistic helper class to parse a line of text into a Displacement.
 * <pre>
 *     0      1      2      3      4      5       6
 *     refx,  refy,  posx,  posy,  diffx, diffy,  type
 *     double,double,double,double,double,double, Category (String)
 * </pre>
 *
 * <ol>
 *   <li>refx = Design / reference coordinate (x)</li>
 *   <li>refy = Design / reference coordinate (y)</li>
 *   <li>posx = Displaced coordinate x (horizontal)</li>
 *   <li>posy = Displaced coordinate y (vertical)</li>
 *   <li>diffx = Difference: posx - refx</li>
 *   <li>diffy = Difference: posy - refy</li>
 *   <li>type = String representing the point category</li>
 * </ol>
 *
 * Default column separator must be a comma. Number format must use period as decimal separator.
 * No thousand separator.
 */
class DisplacementParser implements BiFunction<Integer,String, Displacement>{

	@Override
	public Displacement apply(Integer index, String line) {
		return apply(index, line, ",");
	}

	public Displacement apply(Integer index, String line, String columnSeparatorRegex) {
		String[] e = line.split(columnSeparatorRegex);

		double x = Double.parseDouble(e[0].trim());
		double xd = Double.parseDouble(e[2].trim());
		double y = Double.parseDouble(e[1].trim());
		double yd = Double.parseDouble(e[3].trim());
		
		String type = e[6].trim();
		int firstQuote = type.indexOf("\"");
		int lastQuote = type.lastIndexOf("\"");
		
		Category category = Category.REG;
		if (firstQuote >= 0 && lastQuote >= 0) {
			var siteClass = type.substring(firstQuote+1, lastQuote).trim();
			category = Category.fromString(siteClass);
		} 				
		return Displacement.at(index,index, x, y, xd, yd, category);
	}
}
