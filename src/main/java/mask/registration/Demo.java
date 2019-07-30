package mask.registration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.*;
import java.util.List;
import java.util.function.Predicate;

public class Demo {

	public static void main(String ...args) {
		
		Demo demo = new Demo();
		demo.run();
		

	}

	private void run() {
		List<Displacement> displacements = loadFile("Demo.csv");
		displacements.forEach(System.out::println);
 
		Predicate<Displacement> selection = d -> true; // MULTIPOINT;
		
		
		RigidTransform jamaAlignment = new JamaAlignment().apply(displacements, selection);
		System.out.println("Alignment:");
		System.out.println(jamaAlignment);
			
		AlignmentCorrection alignmentCorrection = new AlignmentCorrection(displacements);
		List<Displacement> rotated = alignmentCorrection.apply(jamaAlignment);
		System.out.println("");
		System.out.println("AfterRotation:");
		rotated.forEach(System.out::println);
		
		TranslationCorrection translationCorrection = new TranslationCorrection();
		List<Displacement> shifted = translationCorrection.apply(rotated);
		System.out.println("");
		System.out.println("AfterTranslation:");
		shifted.forEach(System.out::println);
		
		RigidTransform jamaAfterTranslate2 = new JamaAlignment().apply(shifted, selection);
		System.out.println("");
		System.out.println("Final:");
		System.out.println(jamaAfterTranslate2);
		
	
		
	    System.out.println(Displacement.average(rotated, Displacement::dX));
	    System.out.println(Displacement.average(rotated, Displacement::dY));
	}

	private static List<Displacement> loadFile(String filename) {
		
		try {
			List<String> lines = Files.readAllLines(Paths.get(filename), Charset.forName("UTF-8"));
			List<Displacement> dp = new ArrayList<>(lines.size());
			
			for (String line : lines) {
				try {
					String[] e = line.split(";");
					
					double x = Double.parseDouble(e[4]);
					double xd = Double.parseDouble(e[5]);
					double y = Double.parseDouble(e[6]);
					double yd = Double.parseDouble(e[7]);
					String type = e[14];
					
					SiteClass sc = SiteClass.REG_MARK;
					try {
						sc = SiteClass.valueOf(type);
					} catch (IllegalArgumentException iae) {
						// dont care;
					}
					
					
					Displacement d = Displacement.of(x, y, xd, yd, sc);
					dp.add(d);
				} catch(NumberFormatException nfe) {
					// dont care
				}	
			}
			return dp;
			
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}
