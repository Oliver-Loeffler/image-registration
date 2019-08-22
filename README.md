# Image Placement

## Goals

* Learn how to implement a construction kit for various transforms used in photomask image placement 
  using Javas functional elements
* Experimenting to find suitable data types and data flows for easy use and extendability
* Try to make model parameter names and class names to speak for them selves, ideally 
  end up with a fluent API which uses builder pattern for setup
* The library should behave as lazy as possible 
* It should be numerically and technically correct 
* Try more advanced transforms beyond rigid (alignment) and affine (6-parameter first 
  order). Technically n-parameters higher order should work.
* Decouple matrix computation (equation solving) from high level transform code so 
  that matrix libraries can be exchanged (e.g. using La4J instead of Jama)
* Make all core elements immutable, improve design step by step to achieve concurrency 
  for large data sets (improve speed by using fork-join, try to use async using CompletableFutures) 
* Consider use of Units-of-Measurement API (JSR385)
* Learn how project Valhalla works in Java 14

## Todos
* Handle 1D cases (handling of individual missing points already works)
* Define high level API (ideally fluent or builder based) to compose evaluations.
* Fluent-API for selecting specific displacements for alignment, positional calculation, 
  first order calculation

* Implement higher order polynomial model with configurable coefficients (polynomial 
  model should work up to 9th order, coefficients to be used shall be configurable independently for x and y)
* Sum up learnings and reshape structure of alignment/correction classes, possibly add 
  higher level functions to do the all-in-one-job as its done in the demos.   

* Move to Java 14 and play with project Valhalla (Value Types/Value Objects, JEP169)

## Example Code

```java
    
    /* STEP 1, load displacements from file (or any other source)
     *
     * The CSV file may look like:
     * "refx","refy","posx","posy","diffx","diffy","type"
     * 10980.0,9012.5,10980.014331404400,9012.481628831100,0.014331404400,-0.018371168900,"ALIGN"
     * 10980.0,73512.5,10980.009388937700,73512.484174799200,0.009388937700,-0.015825200800,"REG_MARK"
     * 10980.0,143387.5,10980.004598393400,143387.490394277000,0.004598393400,-0.009605722700,"ALIGN"
     * ....
     * 146975.0,14925.0,146975.030283248000,NaN,0.030283248900,NaN,"INFO_ONLY"
     * 
     * All values are stored with the same unit, in this example all columns consist
     * of values in microns. The values in columns (diffx,diffy) are not used.
     * In case of one-dimensionality or missing values, NaN can be used.
     * 
     */

    List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point.csv"));
        
    /*
     * 
     * Each Displacement consists of a design location (x,y) and the actual displaced
     * location (xd,yd). By default, each Displacement is of type REG, but depending
     * on declaration in CSV file, a different DisplacementClass can be assigned.
     * The DisplacementClass allows to create simple predicates to select Displacmemt  
     * instances for different operations.
     *
     */
    

    // STEP 2, perform site selection
    Predicate<Displacement> allSites = d->true;
     
    SiteSelection selection = SiteSelection
                        .forAlignment(d -> d.isOfType(DisplacementClass.ALIGN))
                        .forCalculation(allSites)
                        .build()
                        .remove(d->d.isOfType(DisplacementClass.INFO_ONLY));
    
    // STEP 3, parametrize evaluation model 
    FirstOrderSetup setup = FirstOrderSetup
                        .usingAlignment(Alignments.SELECTED)
                        .withCompensations(Compensations.SCALE, Compensations.ORTHO)
                        .withSiteSelection(selection);

    // STEP 4, perform correction and calculate results
    FirstOrderResult result = FirstOrderCorrection.using(displacements, setup);
    Collection<Displacement> results = result.getDisplacements();

    /* 
     *  Alternatively:
     *
     *  FirstOrderCorrection correction = new FirstOrderCorrection();
     *  results = correction.apply(displacements, setup).getDisplacements();
     * 
     *  As FirstOrderCorrection is a function, .andThen(...) and .compose(...) 
     *  can be used as well. 
     *
     */  
    
    // STEP 5, print results
        
    // Now print results before correction
    DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, selection.getCalculation());
    System.out.println(uncorrectedSummary);
        
    // after correction
    DisplacementSummary correctedSummary = Displacement.summarize(results, selection.getCalculation());
    System.out.println(correctedSummary);
        
    // now also print residual first order and alignment
    RigidTransform correctedAlignment = result.getAlignment();
    System.out.println(correctedAlignment);
        
    AffineTransform correctedFirstOrder = result.getFirstOrder();
    System.out.println(correctedFirstOrder);
```

## Further Reading
* https://www.fil.ion.ucl.ac.uk/spm/doc/books/hbf2/pdfs/Ch2.pdf
* https://www.uni-muenster.de/AMM/num/Vorlesungen/VarBioMed_WS10/skript/Kapitel_3_4_Registrierung.pdf
* http://www.mathe.tu-freiberg.de/~tochten/gkhm/skript_Matrizen_Gleichungssysteme_Determinanten_ws07.pdf
* https://www.cs.tau.ac.il/~dcor/Graphics/cg-slides/trans3d.pdf 

## Licensing

   Copyright 2019 Oliver Löffler, Raumzeitfalle.net

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

## External libraries used with their licenses

   This project makes use of NIST.gov JAMA library. JAMA is public domain, see:
   https://math.nist.gov/javanumerics/jama/#license.

   Also this project uses LA4J library, which also follows Apache 2.0 license.
   See http://la4j.org for details, sources can be found at https://github.com/vkostyukov/la4j.

 