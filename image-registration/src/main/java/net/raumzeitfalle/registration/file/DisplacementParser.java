package net.raumzeitfalle.registration.file;

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
