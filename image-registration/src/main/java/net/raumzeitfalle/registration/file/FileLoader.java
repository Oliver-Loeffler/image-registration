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
package net.raumzeitfalle.registration.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.*;

import net.raumzeitfalle.registration.displacement.Displacement;


public class FileLoader implements Function<Path,List<Displacement>> {

	private final Logger logger = Logger.getLogger(FileLoader.class.getName());
	
	@Override
	public List<Displacement> apply(Path file) {
		return load(file);
	}
	
	public List<Displacement> load(Path file) {
		try {
			List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
			
			List<Displacement> dp = new ArrayList<>(lines.size());

			DisplacementParser parser = new DisplacementParser();
			
			int index = 1;
			for (String line : lines) {
				if (line.startsWith("\"") && line.toLowerCase().contains("refx")) {
					continue;
				}
				
				try {
					Displacement d = parser.apply(index, line);
					dp.add(d);
					index++;
				} catch(NumberFormatException nfe) {
					logger.log(Level.WARNING, "Could not parse value of (x,xd,y,yd). Either file is incomplete or file format is unknown.");
				}	
			}
			return dp;
			
		} catch (IOException e) {
			throw new DisplacementParsingError(e);
		}
	}

}
