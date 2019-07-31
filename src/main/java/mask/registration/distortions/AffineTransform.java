package mask.registration.distortions;

import java.util.function.Function;

import mask.registration.Displacement;

public class AffineTransform implements Function<Displacement, Displacement> {

    public static AffineTransform with(double transx, double transy,
            double scalex, double scaley,
            double orthox, double orthoy) {
        return new AffineTransform(transx, transy, scalex, scaley, orthox, orthoy);
    }

    private final double translationX;

    private final double translationY;

    private final double scaleX;

    private final double scaleY;

    private final double orthoX;

    private final double orthoY;

    protected AffineTransform(double tx, double ty,
                                double sx, double sy,
                                double ox, double oy) {
        this.translationX = tx;
        this.translationY = ty;
        this.scaleX = sx;
        this.scaleY = sy;
        this.orthoX = ox;
        this.orthoY = oy;
    }

    public double getTranslationX() {
        return translationX;
    }

    public double getTranslationY() {
        return translationY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getOrthoX() {
        return orthoX;
    }

    public double getOrthoY() {
        return orthoY;
    }
    
    @Override
    public String toString() {
        return "FirstOrderTransform [" +
               "tx=" + format(translationX) + " um, sx=" + format(scaleX * 1E6) + " ppm, ox=" + format(orthoX * 1E6) + " urad" +
               System.lineSeparator() + "                     ty=" + format(translationY) + " um, sy=" + format(scaleY * 1E6) + " ppm, oy=" +
               format(orthoY * 1E6) + " urad]";
    }

    protected String format(double value) {
        return String.format("%10.7f", value);
    }

    @Override
    public Displacement apply(Displacement d) {
        throw new UnsupportedOperationException("To be implemented");
    }

}
