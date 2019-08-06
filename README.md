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
* Make all core elements immutable, improve design step by step to achieve concurrency 
  for large data sets (improve speed by using fork-join, try to use async using CompletableFutures) 
* Consider use of Units-of-Measurement API (JSR385)
* Move to Java 14 and play with project Valhalla (Value Types/Value Objects, JEP169)

## Todos

* Define high level API (ideally fluent or builder based) to compose evaluations.
* Fluent-API for selecting specific displacements for alignment, positional calculation, 
  first order calculation
* Implement higher order polynomial model with configurable coefficients (polynomial 
  model should work up to 9th order, coefficients to be used shall be configurable independently for x and y)  
