package mask.registration;

import java.nio.file.Paths;
import java.util.List;

import mask.registration.file.FileLoader;

public class DemoMultipointOneDimensional {

	public static void main(String ...args) {
		
		DemoMultipointOneDimensional demo = new DemoMultipointOneDimensional();
		demo.run();

	}

	private void run() {
		System.out.println(System.lineSeparator() + "--- DEMO: 1-dimensional data ------------------");

		SiteSelection selection = SiteSelection.multipoint();
		List<Displacement> displacements = new FileLoader().load(Paths.get("DemoOneDimensionXonly.csv"));
		
		// TODO: Test & implement handling of 1D cases
	}

	
}
