package mask.registration.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import mask.registration.Displacement;
import mask.registration.SiteClass;

public class FileLoader implements Function<Path,List<Displacement>> {

	@Override
	public List<Displacement> apply(Path file) {
		return load(file);
	}
	
	public List<Displacement> load(Path file) {
		try {
			List<String> lines = Files.readAllLines(file, Charset.forName("UTF-8"));
			List<Displacement> dp = new ArrayList<>(lines.size());
			Set<String> siteClasses = Arrays.stream(SiteClass.values())
											.map(SiteClass::name)
											.collect(Collectors.toSet());
			int index = 1;
			for (String line : lines) {
				try {
					String[] e = line.split(",");
					
					double x = Double.parseDouble(e[0]);
					double xd = Double.parseDouble(e[2]);
					double y = Double.parseDouble(e[1]);
					double yd = Double.parseDouble(e[3]);
					
					String type = e[6];
					int firstQuote = type.indexOf("\"");
					int lastQuote = type.lastIndexOf("\"");
					
					SiteClass sc = SiteClass.REG_MARK;
					if (firstQuote >= 0 && lastQuote >= 0) {
						String siteClass = type.substring(firstQuote+1, lastQuote);
						if (siteClasses.contains(siteClass)) {
							sc = SiteClass.valueOf(siteClass);	
						}
					} 				
					
					Displacement d = Displacement.of(index, x, y, xd, yd, sc);
					dp.add(d);
					index++;
				} catch(NumberFormatException nfe) {
					// ignore corrupted lines
				}	
			}
			return dp;
			
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
