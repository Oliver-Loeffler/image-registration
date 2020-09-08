package net.raumzeitfalle.registration;

public final class Orientations {

	public static final Orientation X = new XOrientation();

	public static final Orientation Y = new YOrientation();

	public static final Orientation XY = new XYOrientation();
	
	private static abstract class OrientationBase implements Orientation {
		protected String orientation;
		
		private OrientationBase(String descriptor) {
			this.orientation = descriptor;
		}
		
		@Override
		public String toString() {
			return this.orientation;
		}
	}

	private static final class XOrientation extends OrientationBase {

		private XOrientation() {
			super("X");
		}
		
		@Override
		public int getDimensions() {
			return 1;
		}

		@Override
		public <T> T runOperation(OrientedOperation<T> operationFactory) {
			return operationFactory.getX();
		}
		
	}

	private static final class YOrientation extends OrientationBase {
		
		private YOrientation() {
			super("Y");
		}
		
		@Override
		public int getDimensions() {
			return 1;
		}

		@Override
		public <T> T runOperation(OrientedOperation<T> operationFactory) {
			return operationFactory.getY();
		}

	}

	private static final class XYOrientation extends OrientationBase {

		private XYOrientation() {
			super("XY");
		}
		
		@Override
		public int getDimensions() {
			return 2;
		}

		@Override
		public <T> T runOperation(OrientedOperation<T> operationFactory) {
			return operationFactory.getCombined();
		}

	}
}
