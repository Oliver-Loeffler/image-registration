package net.raumzeitfalle.jama;

/**
 * Math Utilities
 */
public class Maths {

   private Maths() {
      /* not intended for instantiation */
   }

   /**
    * sqrt(a^2 + b^2) without under/overflow.
    * @param a adjacent length
    * @param b opposite side length
    * @return double hypotenuse length
    */
   public static double hypot(double a, double b) {
      double r;
      if (Math.abs(a) > Math.abs(b)) {
         r = b/a;
         r = Math.abs(a)*Math.sqrt(1+r*r);
      } else if (b != 0) {
         r = a/b;
         r = Math.abs(b)*Math.sqrt(1+r*r);
      } else {
         r = 0.0;
      }
      return r;
   }
}
