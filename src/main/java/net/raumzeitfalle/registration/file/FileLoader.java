/*
 *   Copyright 2019 Oliver Löffler, Raumzeitfalle.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.raumzeitfalle.registration.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.DisplacementClass;


public class FileLoader implements Function<Path,List<Displacement>> {

	private final Logger logger = Logger.getLogger(FileLoader.class.getName());
	
	@Override
	public List<Displacement> apply(Path file) {
		return load(file);
	}
	
	public List<Displacement> load(Path file) {
		try {
			List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));
			
			List<Displacement> dp = new ArrayList<>(lines.size());

			int index = 1;
			for (String line : lines) {
				if (line.startsWith("\"") && line.toLowerCase().contains("refx")) {
					continue;
				}
				
				try {
					String[] e = line.split(",");
					
					double x = Double.parseDouble(e[0]);
					double xd = Double.parseDouble(e[2]);
					double y = Double.parseDouble(e[1]);
					double yd = Double.parseDouble(e[3]);
					
					String type = e[6];
					int firstQuote = type.indexOf("\"");
					int lastQuote = type.lastIndexOf("\"");
					
					DisplacementClass category = DisplacementClass.REG;
					if (firstQuote >= 0 && lastQuote >= 0) {
						String siteClass = type.substring(firstQuote+1, lastQuote);
						category = DisplacementClass.fromString(siteClass);
					} 				
					
					Displacement d = Displacement.at(index,index, x, y, xd, yd, category);
					dp.add(d);
					index++;
				} catch(NumberFormatException nfe) {
					logger.log(Level.WARNING, "Could not parse value of (x,xd,y,yd). Either file is incomplete or file format is unknown.");
				}	
			}
			return dp;
			
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
