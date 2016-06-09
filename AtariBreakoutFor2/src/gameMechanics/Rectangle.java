package gameMechanics;

import java.awt.image.ConvolveOp;
import java.util.ArrayList;
import javax.tools.ToolProvider;

import math.NewMath;
import math.Vector2d;

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

	public static void calculateSides(Rectangle r) {
		r.topSide = new ArrayList<Vector2d>();
		r.rightSide = new ArrayList<Vector2d>();
		r.bottomSide = new ArrayList<Vector2d>();
		r.leftSide = new ArrayList<Vector2d>();
		// r.topSide.clear();
		// r.rightSide.clear();
		// r.bottomSide.clear();
		// r.leftSide.clear();
		// for(int i = 0; i< r.numOfPoints+1;i++){
		// if(r.points.size() > i)
		// r.topSide.add(r.points.get(i));
		// }
		// for(int i = r.numOfPoints+1; i< r.numOfPoints*2+2;i++){
		// if(r.points.size() > i)
		// r.rightSide.add(r.points.get(i));
		// }
		// for(int i = r.numOfPoints*2+2; i< r.numOfPoints*3+3;i++){
		// if(r.points.size() > i)
		// r.bottomSide.add(r.points.get(i));
		// }
		// for(int i = r.numOfPoints*3+3; i< r.points.size();i++){
		// if(r.points.size() > i)
		// r.leftSide.add(r.points.get(i));
		// }

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
	 * calculates the points of the rectangle according to the referencePoint,
	 * the lengths of each side
	 * 
	 * @param lengthOfSideA
	 * @param lengthOfSideB
	 * @param referencePoint
	 * @param numOfPoints
	 * @return
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
		// points from topLeft to topRight
		// for (int i = 0; i < numOfPoints; i++) {
		// Vector2d newPoint = new Vector2d(topLeftPoint.getX() + lengthOfSideB
		// / numOfPoints * i,
		// topLeftPoint.getY());
		// points.add(newPoint);
		// }

		points.add(topRightPoint);

		// points from topRight to bottomRight
		// for (int i = 0; i < numOfPoints; i++) {
		// Vector2d newPoint = new Vector2d(topRightPoint.getX(),
		// topRightPoint.getY() + lengthOfSideA / numOfPoints * i);
		// points.add(newPoint);
		// }

		points.add(bottomRightPoint);
		// points from bottomRight to bottomLeft
		// for (int i = 0; i < numOfPoints; i++) {
		// Vector2d newPoint = new Vector2d(bottomRightPoint.getX() -
		// lengthOfSideB / numOfPoints * i,
		// bottomLeftPoint.getY());
		// points.add(newPoint);
		// }

		points.add(bottomLeftPoint);

		// points from bottomLeft to topLeft
		// for (int i = 0; i < numOfPoints; i++) {
		// Vector2d newPoint = new Vector2d(bottomLeftPoint.getX(),
		// bottomLeftPoint.getY() - lengthOfSideA / numOfPoints * i);
		// points.add(newPoint);
		// }

		return points;
	}

	public double getLengthOfSideA() {
		return lengthOfSideA;
	}

	public void setLengthOfSideA(double length) {
		lengthOfSideA = length;
	}

	public void setLengthOfSideB(double length) {
		lengthOfSideB = length;
	}

	public double getLengthOfSideB() {
		return lengthOfSideB;
	}

	int ijk = 0;
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

	public void rotate(double angle) {
		double prevAngle = this.rotationAngle;
		super.rotate(angle);
		rotateCorners(Math.toRadians(prevAngle),Math.toRadians(angle));
		
		//calculateCornersAndSides();
	}

	/**
	 * returns the position to prevent from Collider to be inside the rectangle
	 * 
	 * @param c
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

	public void calculateNormalizedDirectionOfEachCorner() {
		Vector2d leftBottomToTopDirection = Vector2d.sub(topLeftPoint, bottomLeftPoint);
		Vector2d topLeftToRightDirection = Vector2d.sub(topRightPoint, topLeftPoint);
		Vector2d rightBottomToTopDirection = Vector2d.sub(topRightPoint, bottomRightPoint);
		Vector2d bottomRightToLeftDirection = Vector2d.sub(bottomLeftPoint, bottomRightPoint);

		//System.out.println("topLeftPont"+topLeftPoint+", toprightp"+topRightPoint+"toplefttorightdir"+topLeftToRightDirection+"leftBottomToTopDirection"+leftBottomToTopDirection);
//		Vector2d topLeftCorner = Vector2d.add(Vector2d.mul(-1, topLeftToRightDirection).normalized(),
//				leftBottomToTopDirection.normalized());
//		Vector2d topRightCorner = Vector2d.add(topLeftToRightDirection.normalized(),
//				rightBottomToTopDirection.normalized());
//		Vector2d bottomLeftCorner = Vector2d.add(Vector2d.mul(-1, leftBottomToTopDirection).normalized(),
//				bottomRightToLeftDirection.normalized());
//		Vector2d bottomRightCorner = Vector2d.add(Vector2d.mul(-1, rightBottomToTopDirection).normalized(),
//				Vector2d.mul(-1, bottomRightToLeftDirection.normalized()));

//		Vector2d topLeftCorner = Vector2d.add(Vector2d.mul(-1, topLeftToRightDirection).normalized(),
//				rightBottomToTopDirection.normalized());
//		Vector2d topRightCorner = Vector2d.add(topLeftToRightDirection.normalized(),
//				leftBottomToTopDirection.normalized());
//		Vector2d bottomLeftCorner = Vector2d.add(Vector2d.mul(-1, leftBottomToTopDirection).normalized(),
//				Vector2d.mul(-1,topLeftToRightDirection).normalized());
//		Vector2d bottomRightCorner = Vector2d.add(topLeftToRightDirection.normalized(),
//				Vector2d.mul(-1, rightBottomToTopDirection.normalized()));

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
		//Vector2d topLeftCorner = Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(1,Vector2d.sub(topLeftPoint, topRightPoint).normalized()),topLeftPoint),topLeftPoint,Math.toRadians(-45)).normalized(); 
		//Vector2d topLeftCorner = Vector2d.sub(Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(-1, topLeftToRightDirection).normalized(),topLeftPoint), topLeftPoint, Math.toRadians(-45)),topLeftPoint).normalized();
		//Vector2d topRightCorner = Vector2d.sub(Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(1, topLeftToRightDirection).normalized(),topRightPoint), topRightPoint, Math.toRadians(-45)),topRightPoint).normalized();
		//Vector2d bottomLeftCorner = Vector2d.sub(Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(1, bottomRightToLeftDirection).normalized(),bottomLeftPoint), bottomLeftPoint, Math.toRadians(-45)),bottomLeftPoint).normalized();
//		Vector2d bottomRightCorner = Vector2d.sub(Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(-1, bottomRightToLeftDirection).normalized(),bottomRightPoint), bottomRightPoint, Math.toRadians(-45)),bottomRightPoint).normalized();

//		Vector2d topLeftCorner = Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(-1, topLeftToRightDirection).normalized(),topLeftPoint), topLeftPoint, Math.toRadians(45)).normalized();
//		Vector2d topRightCorner = Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(1, topLeftToRightDirection).normalized(),topRightPoint), topRightPoint, Math.toRadians(45)).normalized();
//		Vector2d bottomLeftCorner = Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(1, bottomRightToLeftDirection).normalized(),bottomLeftPoint), bottomLeftPoint, Math.toRadians(45)).normalized();
//		Vector2d bottomRightCorner =Vector2d.rotatePointAroundAnotherPoint(Vector2d.add(Vector2d.mul(-1, bottomRightToLeftDirection).normalized(),bottomRightPoint), bottomRightPoint, Math.toRadians(45)).normalized();
//		System.out.println("topleftdir"+topLeftCorner+", toprightdir"+topRightCorner+", bottomleftdir"+
//		bottomLeftCorner+", bottomrightdir"+bottomRightCorner);
		this.directionBottomLeft = bottomLeftCorner;
		this.directionBottomRight = bottomRightCorner;
		this.directionTopLeft = topLeftCorner;
		this.directionTopRight = topRightCorner;
		
	}

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
	 * sets the LengthOfSideA to newLengthOfSideA, sets the LengthOfSideB to
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
