package gameMechanics;

import java.awt.image.ConvolveOp;
import java.util.ArrayList;
import javax.tools.ToolProvider;

import math.NewMath;
import math.Vector2d;

/**
 * @author Ethan
 * the Rectangle shape that extends {@link Shape}
 */
public class Rectangle extends Shape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8033392852267232882L;
	private double lengthOfSideA, lengthOfSideB; // the sizes of each side of
													// the rectangles
	private int numOfPoints;
	public ArrayList<Vector2d> leftSide, rightSide, topSide, bottomSide;
	public Vector2d leftSideVector, rightSideVector, topSideVector, bottomSideVector, topLeftPoint, topRightPoint,
			bottomRightPoint, bottomLeftPoint, directionTopLeft, directionTopRight, directionBottomRight,
			directionBottomLeft;

	/**
	 * 
	 * @param points
	 *            the list of all the points
	 * @param referencePoint
	 *            the center point of the rectangle
	 * @param lengthOfSideA
	 *            is considered to be the height/ y axis length
	 * @param lengthOfSideBis
	 *            considered to be the width/ x axis length
	 */
	public Rectangle(Vector2d referencePoint, double lengthOfSideA, double lengthOfSideB, int numOfPoints) {
		super(calculateRectanglePoints(lengthOfSideA, lengthOfSideB, referencePoint, numOfPoints), referencePoint);
		this.lengthOfSideA = lengthOfSideA;
		this.lengthOfSideB = lengthOfSideB;
		this.numOfPoints = numOfPoints;
		topSide = new ArrayList<Vector2d>();
		rightSide = new ArrayList<Vector2d>();
		bottomSide = new ArrayList<Vector2d>();
		leftSide = new ArrayList<Vector2d>();
		setShapeHeight(lengthOfSideA);
		setShapeWidth(lengthOfSideB);
		topLeftPoint = points.get(0);
		topRightPoint = points.get(1);
		bottomLeftPoint = points.get(2);
		bottomRightPoint = points.get(3);
		calculateSides(this);
		calculateNormalizedDirectionOfEachCorner();
	}

	public Rectangle(Rectangle r) {
		super(r);
		this.numOfPoints = r.numOfPoints;
		this.lengthOfSideA = r.lengthOfSideA;
		this.lengthOfSideB = r.lengthOfSideB;
		this.topLeftPoint = new Vector2d(r.topLeftPoint);
		this.topRightPoint = new Vector2d(r.topRightPoint);
		this.bottomRightPoint = new Vector2d(r.bottomRightPoint);
		this.bottomLeftPoint = new Vector2d(r.bottomLeftPoint);
		this.topSide = new ArrayList<Vector2d>(r.topSide);
		this.topSideVector = new Vector2d(r.topSideVector);
		this.rightSide = new ArrayList<Vector2d>(r.rightSide);
		this.rightSideVector = new Vector2d(r.rightSideVector);
		this.bottomSide = new ArrayList<Vector2d>(r.bottomSide);
		this.bottomSideVector = new Vector2d(r.bottomSideVector);
		this.leftSide = new ArrayList<Vector2d>(r.leftSide);
		this.leftSideVector = new Vector2d(r.leftSideVector);
		this.directionBottomLeft = r.directionBottomLeft;
		this.directionBottomRight = r.directionBottomRight;
		this.directionTopLeft = r.directionTopLeft;
		this.directionTopRight = r.directionTopRight;
		//calculateNormalizedDirectionOfEachCorner();
		
		this.rotationAngle = r.rotationAngle;
	}

	/**
	 * @return {@link Rectangle#numOfPoints}
	 */
	public int getNumOfPoints() {
		return numOfPoints;
	}

	@Override
	public Vector2d getPositionRelativeToJavaGraphics() {
		return Vector2d.add(referencePoint, new Vector2d(-lengthOfSideB / 2, -lengthOfSideA / 2));
	}

	@Override
	public Vector2d convertPositionToWorkWithJavaGraphics() {
		// TODO Auto-generated method stub
		// return Vector2d.add(super.convertPositionToWorkWithJavaGraphics(),new
		// Vector2d(-lengthOfSideB / 2, -lengthOfSideA / 2));
		return Vector2d.add(referencePoint, new Vector2d(-lengthOfSideB / 2, -lengthOfSideA / 2));
	}

	/** calculates all the {@link Vector2d}'s that represent the sides of the rectangle, using
	 * their four cornerpoint {@link Vector2d}s.
	 * @param r the {@link Rectangle} in mind
	 */
	public static void calculateSides(Rectangle r) {
		r.topSide = new ArrayList<Vector2d>();
		r.rightSide = new ArrayList<Vector2d>();
		r.bottomSide = new ArrayList<Vector2d>();
		r.leftSide = new ArrayList<Vector2d>();
		
		Vector2d topLeftPoint = new Vector2d(
				Vector2d.add(r.referencePoint, new Vector2d(-r.lengthOfSideB / 2, -r.lengthOfSideA / 2)));
		Vector2d topRightPoint = new Vector2d(
				Vector2d.add(r.referencePoint, new Vector2d(r.lengthOfSideB / 2, -r.lengthOfSideA / 2)));
		Vector2d bottomLeftPoint = new Vector2d(
				Vector2d.add(r.referencePoint, new Vector2d(-r.lengthOfSideB / 2, r.lengthOfSideA / 2)));
		Vector2d bottomRightPoint = new Vector2d(
				Vector2d.add(r.referencePoint, new Vector2d(r.lengthOfSideB / 2, r.lengthOfSideA / 2)));

		r.topLeftPoint = topLeftPoint;
		r.topRightPoint = topRightPoint;
		r.bottomRightPoint = bottomRightPoint;
		r.bottomLeftPoint = bottomLeftPoint;

		r.topSide.add(topLeftPoint);
		r.topSide.add(topRightPoint);

		r.rightSide.add(topRightPoint);
		r.rightSide.add(bottomRightPoint);
		r.bottomSide.add(bottomLeftPoint);
		r.bottomSide.add(bottomRightPoint);
		r.leftSide.add(topLeftPoint);
		r.leftSide.add(bottomLeftPoint);
		r.bottomSideVector = Vector2d.sub(bottomLeftPoint, bottomRightPoint);
		r.rightSideVector = Vector2d.sub(topRightPoint, bottomRightPoint);
		r.topSideVector = Vector2d.sub(topLeftPoint, topRightPoint);
		r.leftSideVector = Vector2d.sub(topLeftPoint, bottomLeftPoint);
	}

	@Deprecated
	public Vector2d returnSideOfRectangleWithPointIndex(int i) {
		if (i < numOfPoints + 1) {
			return topSideVector;
		} else if (i >= numOfPoints + 1 && i < numOfPoints * 2 + 1) {
			return rightSideVector;
		} else if (i >= numOfPoints * 2 + 2 && i < numOfPoints * 3 + 2) {
			return bottomSideVector;
		} else {
			return leftSideVector;
		}
	}

	/**
	 * calculates the {@link Vector2d} points of the {@link Rectangle} according to the {@link Vector2d} referencePoint,
	 * the lengths of each side
	 * @param lengthOfSideA represents the height
	 * @param lengthOfSideB represents the width
	 * @param referencePoint the {@link Vector2d} center point
	 * @param numOfPoints
	 * @return the {@link Vector2d} points of the {@link Rectangle}
	 */
	public static ArrayList<Vector2d> calculateRectanglePoints(double lengthOfSideA, double lengthOfSideB,
			Vector2d referencePoint, int numOfPoints) {
		ArrayList<Vector2d> points = new ArrayList<Vector2d>();
		Vector2d topLeftPoint = new Vector2d(
				Vector2d.add(referencePoint, new Vector2d(-lengthOfSideB / 2, -lengthOfSideA / 2)));

		Vector2d topRightPoint = new Vector2d(
				Vector2d.add(referencePoint, new Vector2d(lengthOfSideB / 2, -lengthOfSideA / 2)));
		Vector2d bottomLeftPoint = new Vector2d(
				Vector2d.add(referencePoint, new Vector2d(-lengthOfSideB / 2, lengthOfSideA / 2)));
		Vector2d bottomRightPoint = new Vector2d(
				Vector2d.add(referencePoint, new Vector2d(lengthOfSideB / 2, lengthOfSideA / 2)));

		points.add(topLeftPoint);

		points.add(topRightPoint);

		points.add(bottomRightPoint);
		
		points.add(bottomLeftPoint);

		return points;
	}

	/**
	 * @return {@link Rectangle#lengthOfSideA}
	 */
	public double getLengthOfSideA() {
		return lengthOfSideA;
	}
	
	/**
	 * @param length the height to set
	 */
	public void setLengthOfSideA(double length) {
		lengthOfSideA = length;
	}

	/**
	 * @param length the width to set
	 */
	public void setLengthOfSideB(double length) {
		lengthOfSideB = length;
	}
	/**
	 * @return {@link Rectangle#lengthOfSideB}
	 */
	public double getLengthOfSideB() {
		return lengthOfSideB;
	}

	int ijk = 0;
	/** rotates the {@link Rectangle} {@link Vector2d} corner points
	 * @param prevAngle the previous angle in degrees
	 * @param angle the angle to rotate in degrees
	 */
	public void rotateCorners(double prevAngle,double angle) {
		

		double deltaAngle = -prevAngle;
		
		topLeftPoint = Vector2d.rotatePointAroundAnotherPoint(topLeftPoint, referencePoint, deltaAngle);
		topRightPoint = Vector2d.rotatePointAroundAnotherPoint(topRightPoint, referencePoint, deltaAngle);
		bottomLeftPoint = Vector2d.rotatePointAroundAnotherPoint(bottomLeftPoint, referencePoint, deltaAngle);
		bottomRightPoint = Vector2d.rotatePointAroundAnotherPoint(bottomRightPoint, referencePoint, deltaAngle);

		//System.out.println(this.referencePoint+"bottomrightpoint1"+bottomRightPoint);
		topLeftPoint = Vector2d.rotatePointAroundAnotherPoint(topLeftPoint, referencePoint, angle);
		topRightPoint = Vector2d.rotatePointAroundAnotherPoint(topRightPoint, referencePoint, angle);
		bottomLeftPoint = Vector2d.rotatePointAroundAnotherPoint(bottomLeftPoint, referencePoint, angle);
		bottomRightPoint = Vector2d.rotatePointAroundAnotherPoint(bottomRightPoint, referencePoint, angle);
		//System.out.println("topleftp"+topLeftPoint+"toprightp"+topRightPoint+"ijk"+ijk);
		calculateNormalizedDirectionOfEachCorner();
		calculateCornersAndSides();
		//System.out.println(this.referencePoint+"bottomrightpoint2"+bottomRightPoint);
	}

	@Override
	public void rotate(double angle) {
		double prevAngle = this.rotationAngle;
		super.rotate(angle);
		rotateCorners(Math.toRadians(prevAngle),Math.toRadians(angle));
		
		//calculateCornersAndSides();
	}

	/**
	 * returns the position to prevent from Collider to be inside the rectangle
	 * 
	 * @param c The {@link Collider}
	 */
	public Vector2d returnCornerPositionCorrection(Collider c) {
		Shape s = c.getColliderShape();
		if (s instanceof Circle) {
			Circle circle = (Circle) s;
			// top left corner
			if (Collider.checksNearWhichCornerOfRect(this, s) == null)
				return null;
			if (Collider.checksNearWhichCornerOfRect(this, s).equals(topLeftPoint)) {
				double distanceOfTopLeftToCenter = Vector2d.findDistanceBetweenTwoVector2ds(topLeftPoint,
						this.referencePoint);
				double distanceOfCircleToCenter = Vector2d.findDistanceBetweenTwoVector2ds(circle.referencePoint,
						this.referencePoint);
				if (distanceOfCircleToCenter < distanceOfTopLeftToCenter + circle.getRadius()) {
					if (NewMath.distanceBetweenPointAndLine(circle.referencePoint, topSide) < NewMath
							.distanceBetweenPointAndLine(circle.referencePoint, leftSide)) {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint, topRightPoint,
								circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(circle.getRadius(),Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint,
						// topRightPoint, circle.referencePoint).normalized()));
						return newPosition;

					} else {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint, bottomLeftPoint,
								circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint, normal);
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					}
				}
			} else if (Collider.checksNearWhichCornerOfRect(this, s).equals(topRightPoint)) {
				double distanceOfTopRightToCenter = Vector2d.findDistanceBetweenTwoVector2ds(topRightPoint,
						this.referencePoint);
				double distanceOfCircleToCenter = Vector2d.findDistanceBetweenTwoVector2ds(circle.referencePoint,
						this.referencePoint);
				if (distanceOfCircleToCenter < distanceOfTopRightToCenter + circle.getRadius()) {
					if (NewMath.distanceBetweenPointAndLine(circle.referencePoint, topSide) < NewMath
							.distanceBetweenPointAndLine(circle.referencePoint, rightSide)) {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint, topRightPoint,
								circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint,
						// topRightPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;

					} else {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topRightPoint,
								bottomRightPoint, circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(topRightPoint,
						// bottomRightPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					}
				}
			} else if (Collider.checksNearWhichCornerOfRect(this, s).equals(bottomLeftPoint)) {
				double distanceOfBottomLeftToCenter = Vector2d.findDistanceBetweenTwoVector2ds(bottomLeftPoint,
						this.referencePoint);
				double distanceOfCircleToCenter = Vector2d.findDistanceBetweenTwoVector2ds(circle.referencePoint,
						this.referencePoint);
				if (distanceOfCircleToCenter < distanceOfBottomLeftToCenter + circle.getRadius()) {
					if (NewMath.distanceBetweenPointAndLine(circle.referencePoint, bottomSide) < NewMath
							.distanceBetweenPointAndLine(circle.referencePoint, leftSide)) {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(bottomLeftPoint,
								bottomRightPoint, circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(bottomLeftPoint,
						// bottomRightPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					} else {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint, bottomLeftPoint,
								circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(topLeftPoint,
						// bottomLeftPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					}
				}
			} else if (Collider.checksNearWhichCornerOfRect(this, s).equals(bottomRightPoint)) {
				double distanceOfBottomRightToCenter = Vector2d.findDistanceBetweenTwoVector2ds(bottomRightPoint,
						this.referencePoint);
				double distanceOfCircleToCenter = Vector2d.findDistanceBetweenTwoVector2ds(circle.referencePoint,
						this.referencePoint);
				if (distanceOfCircleToCenter < distanceOfBottomRightToCenter + circle.getRadius()) {
					if (NewMath.distanceBetweenPointAndLine(circle.referencePoint, bottomSide) < NewMath
							.distanceBetweenPointAndLine(circle.referencePoint, rightSide)) {
						// the other side of topside perpendicular to the
						// topside
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(bottomLeftPoint,
								bottomRightPoint, circle.referencePoint);
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(bottomLeftPoint,
						// bottomRightPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					} else {
						Vector2d normal = Vector2d.findPointThatNormalOfAPointToALineMeet(topRightPoint,
								bottomRightPoint, circle.referencePoint);
						// the other side of topside perpendicular to the
						// topside
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.findPointThatNormalOfAPointToALineMeet(topRightPoint,
						// bottomRightPoint, circle.referencePoint));
						Vector2d newPosition = Vector2d.add(circle.referencePoint,
								Vector2d.add(Vector2d.mul(4 * circle.getRadius(), normal.normalized()), normal));
						// Vector2d newPosition =
						// Vector2d.add(circle.referencePoint,
						// Vector2d.mul(4*circle.getRadius(),
						// normal.normalized()));
						return newPosition;
					}
				}
			} else if (s instanceof Rectangle) {
				// todo
			}
		}
		return null;
	}

	/**
	 *  calculates the normalized {@link Vector2d} direction of each corner.
	 */
	public void calculateNormalizedDirectionOfEachCorner() {
		Vector2d leftBottomToTopDirection = Vector2d.sub(topLeftPoint, bottomLeftPoint);
		Vector2d topLeftToRightDirection = Vector2d.sub(topRightPoint, topLeftPoint);
		Vector2d rightBottomToTopDirection = Vector2d.sub(topRightPoint, bottomRightPoint);
		Vector2d bottomRightToLeftDirection = Vector2d.sub(bottomLeftPoint, bottomRightPoint);

		double da = Vector2d.findDistanceBetweenTwoVector2ds(topLeftPoint, topRightPoint);
		double db = Vector2d.findDistanceBetweenTwoVector2ds(topLeftPoint, bottomLeftPoint);
		double dMin = Math.min(da, db);
		Vector2d topVector = Vector2d.mul(dMin,Vector2d.sub(topLeftPoint, topRightPoint).normalized());
		Vector2d leftVector = Vector2d.mul(dMin,Vector2d.sub(topLeftPoint, bottomLeftPoint).normalized());
		Vector2d bottomVector = Vector2d.mul(dMin,Vector2d.sub(bottomLeftPoint, bottomRightPoint).normalized());
		Vector2d rightVector = Vector2d.mul(dMin,Vector2d.sub(topRightPoint, bottomRightPoint).normalized());
		Vector2d topLeftCorner = Vector2d.add(topVector, leftVector).normalized();
		Vector2d bottomLeftCorner = Vector2d.add(bottomVector, new Vector2d(-leftVector.getX(),-leftVector.getY())).normalized();
		Vector2d topRightCorner = Vector2d.add(rightVector, new Vector2d(-topVector.getX(),-topVector.getY())).normalized();
		Vector2d bottomRightCorner = Vector2d.add(bottomVector, new Vector2d(-rightVector.getX(),-rightVector.getY())).normalized();

		this.directionBottomLeft = bottomLeftCorner;
		this.directionBottomRight = bottomRightCorner;
		this.directionTopLeft = topLeftCorner;
		this.directionTopRight = topRightCorner;
		
	}

	/**
	 * calculates the {@link Vector2d} of each side and corner
	 */
	public void calculateCornersAndSides(){
		topSide = new ArrayList<Vector2d>();
		rightSide = new ArrayList<Vector2d>();
		bottomSide = new ArrayList<Vector2d>();
		leftSide = new ArrayList<Vector2d>();

		
		// calculateSides
		topSide.add(topLeftPoint);
		topSide.add(topRightPoint);

		rightSide.add(topRightPoint);
		rightSide.add(bottomRightPoint);
		bottomSide.add(bottomLeftPoint);
		bottomSide.add(bottomRightPoint);
		leftSide.add(topLeftPoint);
		leftSide.add(bottomLeftPoint);
		bottomSideVector = Vector2d.sub(bottomRightPoint, bottomLeftPoint);
		rightSideVector = Vector2d.sub(topRightPoint, bottomRightPoint);
		topSideVector = Vector2d.sub(topLeftPoint, topRightPoint);
		leftSideVector = Vector2d.sub(topLeftPoint, bottomLeftPoint);
		calculateNormalizedDirectionOfEachCorner();
		
	}
	
	@Override
	public void moveReferencePoint(Vector2d deltaPosition) {
		referencePoint = Vector2d.add(referencePoint, deltaPosition);
		// point = Vector2d.add(Vector2d.add(point,newCirclePosition), new
		// Vector2d(radius,radius));
		// point = Vector2d.add(point,referencePoint);
		for(int i = 0; i<points.size();i++){
			points.set(i,Vector2d.add(points.get(i), deltaPosition));
		}

		topLeftPoint = Vector2d.add(topLeftPoint, deltaPosition);
		topRightPoint = Vector2d.add(topRightPoint, deltaPosition);
		bottomRightPoint = Vector2d.add(bottomRightPoint, deltaPosition);
		bottomLeftPoint = Vector2d.add(bottomLeftPoint, deltaPosition);
		
		calculateCornersAndSides();
		//points = calculateRectanglePoints(lengthOfSideA, lengthOfSideB, referencePoint, numOfPoints);
		
		//rotate(rotationAngle);
		//calculateSides(this);
		// }
	}

	/**
	 * sets the {@link Rectangle#LengthOfSideA} to newLengthOfSideA, sets the {@link Rectangle#LengthOfSideB} to
	 * newLengthOfSideB
	 * 
	 * @param newLengthOfSideA
	 * @param newLengthOfSideB
	 */
	public void changeSizes(double newLengthOfSideA, double newLengthOfSideB) {
		this.lengthOfSideA = newLengthOfSideA;
		this.lengthOfSideB = newLengthOfSideB;
		this.points.clear();
		this.points.addAll(
				calculateRectanglePoints(newLengthOfSideA, newLengthOfSideB, this.referencePoint, this.numOfPoints));
	}
}
