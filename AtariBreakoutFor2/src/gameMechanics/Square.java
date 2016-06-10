package gameMechanics;

import java.util.ArrayList;

import math.Vector2d;

@Deprecated
public class Square extends Rectangle {

	private double lengthOfSide;
	private int numOfPoints;

	public Square(ArrayList<Vector2d> points, Vector2d referencePoint, double lengthOfSide, int numOfPoints) {
		super(referencePoint, lengthOfSide, lengthOfSide, numOfPoints);
		this.setLengthOfSide(lengthOfSide);
	}

	/**
	 * @return the lengthOfSide
	 */
	public double getLengthOfSide() {
		return lengthOfSide;
	}

	/**
	 * calculates and updates the points according to the new lengthOfSide
	 * 
	 * @param lengthOfSide
	 *            the lengthOfSide to set
	 */
	public void setLengthOfSide(double lengthOfSide) {
		this.lengthOfSide = lengthOfSide;
		this.points.clear();
		this.points.addAll(calculateRectanglePoints(lengthOfSide, lengthOfSide, referencePoint, numOfPoints));
	}
	
	@Override
	public void moveReferencePoint(Vector2d newReferencePoint) {
		super.moveReferencePoint(newReferencePoint);
	}
}
