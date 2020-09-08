package net.raumzeitfalle.registration;

import java.util.Collection;

public final class Orientations {

	public static final Orientation X = new XOrientation();

	public static final Orientation Y = new YOrientation();

	public static final Orientation XY = new XYOrientation();
	
	public <N> Orientation determine(Collection<N> x, Collection<N> y) {
		return determine(x.size(), y.size());
	}
	
	public Orientation determine(int xCount, int yCount) {
		if (0 == xCount && 0 == yCount) {
			return XY;
		}
		
		if (0 == xCount) {
			return Y;
		}
		
		if (0 == yCount) {
			return X;
		}
		
		return XY;
	}
	
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
