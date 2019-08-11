package mask.registration.distortions;

public class AffineTransformBuilder {
	
	private final double cx;
	private final double cy;
	
	private final double tx;
	private final double ty;
	
	private double scaleX;
	private double scaleY;
	private double magnification;
	
	private double orthoX;
	private double orthoY;
	
	public AffineTransformBuilder(AffineTransform firstOrder) {
		 this.scaleX = firstOrder.getScaleX();
	     this.scaleY = firstOrder.getScaleY();
	     this.magnification = firstOrder.getMagnification();
	     
	     this.orthoX = firstOrder.getOrthoX();
	     this.orthoY = firstOrder.getOrthoY();
	     
	     this.tx = firstOrder.getTranslationX();
	     this.ty = firstOrder.getTranslationY();
	     
	     this.cx = firstOrder.getCenterX();
	     this.cy = firstOrder.getCenterY();
	}

	public AffineTransform build() {
		if (scaleX == 0.0 && scaleY == 0.0 && orthoX == 0.0 && orthoY == 0.0) {
			return new SkipAffineTransform();
		}
		return new AffineTransform(tx, ty, scaleX, scaleY, orthoX, orthoY, cx, cy);
	}

	public AffineTransformBuilder disableOrthoXY() {
		this.orthoX = 0.0;
		this.orthoY = 0.0;
		return this;
	}

	public AffineTransformBuilder useMagnification() {
		this.scaleX = this.magnification;
		this.scaleY = this.magnification;
		return this;
	}

	public AffineTransformBuilder disableScaleXY() {
		this.scaleX = 0.0;
		this.scaleY = 0.0;
		return this;
	}

	public AffineTransformBuilder disableAll() {
		
		this.scaleX = 0.0;
		this.scaleY = 0.0;
		
		this.orthoX = 0.0;
		this.orthoY = 0.0;
		
		return this;
	}

}
