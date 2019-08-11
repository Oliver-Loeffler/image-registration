package mask.registration.distortions;

public class FirstOrderTransform extends AffineTransform {

    public static FirstOrderTransform with(double transx, double transy,
            double scalex, double scaley,
            double orthox, double orthoy,
            double rotation, double meanx,
            double meany) {
        return new FirstOrderTransform(transx, transy, scalex, scaley, orthox, orthoy, rotation, meanx, meany);
    }
    
    private final double rotation;

    private FirstOrderTransform(double tx, double ty,
                                double sx, double sy,
                                double ox, double oy,
                                double rot, double mx,
                                double my) {
        super(tx,ty,sx,sy,ox,oy, mx, my);
        this.rotation = rot;
    }

    public double getRotation() {
        return rotation;
    }
    
    @Override
    public String toString() {
        return "FirstOrderTransform [" +
               "tx=" + format(getTranslationX()) + " um, sx=" + format(getScaleX() * 1E6) + " ppm, ox=" + format(getOrthoX() * 1E6) + " urad" +
               System.lineSeparator() + "                     ty=" + format(getTranslationY()) + " um, sy=" + format(getScaleY() * 1E6) + " ppm, oy=" +
               format(getOrthoY() * 1E6) + " urad" +
               System.lineSeparator() + "                    rot=" + format(rotation * 1E6) + " urad ]";
    }

}
