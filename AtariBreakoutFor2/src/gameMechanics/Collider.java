package gameMechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.omg.Messaging.SyncScopeHelper;

import gamePieces.GameObject;
import math.NewMath;
import math.Vector2d;

public class Collider {

	private Shape collider; // the shape of the collider. Using the points we
							// can detect collision.
	private boolean drawCollider; // i.e. should this collider be visible?
	private int colliderID; // the colliderID of the collider, this is how you
							// could find a specific collider
	private PhysicsObject parent = null;
	private boolean isCollidable = true;

	public Collider(Shape shape, int colliderID) {
		this.collider = shape;
		drawCollider = true;
		this.setColliderID(colliderID);

	}

	
	public ArrayList<Vector2d> convertAllPointsToJavaGraphics(ArrayList<Vector2d> points){
		ArrayList<Vector2d> newPoints = new ArrayList<Vector2d>();
		for (Vector2d point : points) {
			newPoints.add(point.convertToJavaGraphicsPosition(point, this));
		}
		return newPoints;
	}
	public Collider(Collider newCollider) {
		
		if(newCollider.getColliderShape() instanceof Circle){
			Circle c = (Circle)newCollider.getColliderShape();
		this.collider = new Circle(c.getRadius(), c.getNumOfPoints(), new Vector2d(c.referencePoint)) ;
		}else if(newCollider.getColliderShape() instanceof Rectangle){
			Rectangle r = (Rectangle)newCollider.getColliderShape();
			this.collider = new Rectangle(new Vector2d(r.referencePoint),r.getLengthOfSideA(),r.getLengthOfSideB(),r.getNumOfPoints());
		}
		else{
			this.collider = new Shape(newCollider.getColliderShape());
		}
		this.drawCollider = newCollider.drawCollider;
		this.colliderID = newCollider.colliderID;
	}

	/**
	 * 
	 * @return colliderShape
	 */
	public Shape getColliderShape() {
		return collider;
	}

	/**
	 *  checks which side of the first shape is touching the second shape
	 * @param s1
	 * @param s2
	 * @return the side of the rectangle instance (s1 or s2)
	 */
	public static Vector2d checkWhichSideOfShapeShapeIsOn(Shape s1, Shape s2){
		if(s1 instanceof Rectangle && s2 instanceof Circle){
			Circle c = (Circle)s2;
			Rectangle r = (Rectangle)s1;
			return checkWhichSideOfRectCircleIsOn(r, c);
		}else if(s2 instanceof Rectangle && s1 instanceof Circle){
			Circle c = (Circle)s1;
			Rectangle r = (Rectangle)s2;
			return checkWhichSideOfRectCircleIsOn(r, c);
		}else if(s1 instanceof Rectangle && s2 instanceof Rectangle){
			Rectangle r1 = (Rectangle)s1;
			Rectangle r2 = (Rectangle)s2;
			return checkWhichSideOfRectRectIsOn(r1, r2);
		}
		return null;
	}
	
	/**
	 * checks which side of the first rectangle the second rectangle is touching 
	 * @param r1 the first rectangle
	 * @param r2 the second
	 * @return the side of r1 that r2 is touching
	 */
	public static Vector2d checkWhichSideOfRectRectIsOn(Rectangle r1, Rectangle r2){
		double w = 0.5 * (r1.getLengthOfSideB() + r2.getLengthOfSideB());
		double h = 0.5 * (r1.getLengthOfSideA() + r2.getLengthOfSideA());
		double dx = r1.getReferencePoint().getX() - r2.getReferencePoint().getX();
		double dy = r1.getReferencePoint().getY() - r2.getReferencePoint().getY();

		if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
			/* collision! */
			double wy = w * dy;
			double hx = h * dx;

			if (wy > hx)
				if (wy > -hx) {
					/* collision at the top */
					return r1.topSideVector;
				} else {
					/* on the left */
					return r1.leftSideVector;
				}
			else if (wy > -hx) {
				/* on the right */
				return r1.rightSideVector;
			} else {
				/* at the bottom */
				return r1.bottomSideVector;
			}
		}
		return null;		
	}
	
	/**
	 *  checks which side of the rectangle the circle is touching
	 * @param r the rectangle
	 * @param c the circle
	 * @return the side of the rectangle that the circle touched
	 */
	public static Vector2d checkWhichSideOfRectCircleIsOn(Rectangle r, Circle c) {
		double w = 0.5 * (r.getLengthOfSideB() + c.getRadius()*2);
		double h = 0.5 * (r.getLengthOfSideA() + c.getRadius()*2);
		double dx = r.getReferencePoint().getX() - c.getReferencePoint().getX();
		double dy = r.getReferencePoint().getY() - c.getReferencePoint().getY();

		if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
			/* collision! */
			double wy = w * dy;
			double hx = h * dx;

			if (wy > hx)
				if (wy > -hx) {
					/* collision at the top */
					return r.topSideVector;
				} else {
					/* on the left */
					return r.leftSideVector;
				}
			else if (wy > -hx) {
				/* on the right */
				return r.rightSideVector;
			} else {
				/* at the bottom */
				return r.bottomSideVector;
			}
		}
		return null;
	}

	
//	public static ArrayList<Vector2d> checksNearWhichSideOfRect(Rectangle r, Shape s){
//		double disTop = NewMath.distanceBetweenPointAndLine(s.referencePoint, r.topSide);
//		double disBottom = NewMath.distanceBetweenPointAndLine(s.referencePoint, r.bottomSide);
//		double disLeft = NewMath.distanceBetweenPointAndLine(s.referencePoint, r.leftSide);
//		double disRight = NewMath.distanceBetweenPointAndLine(s.referencePoint, r.rightSide);
//		if(s instanceof Circle){
//			Circle c = (Circle)s;
//		if(disTop <= c.getRadius()/2){
//			return r.topSide;
//		}else if(disRight <= c.getRadius()/2){
//			return r.rightSide;
//		}else if(disBottom <= c.getRadius()/2){
//			return r.bottomSide;
//		}else if(disLeft <= c.getRadius()/2){
//			return r.leftSide;
//		}else{
//			return null;
//		}
//		}else if(s instanceof Rectangle){
//		//todo	
//		}
//		return null;
//	}
	public static Vector2d checksNearWhichCornerOfRect(Rectangle r,Shape s){
		double disTopLeft = Vector2d.findDistanceBetweenTwoVector2ds(r.topLeftPoint, s.getReferencePoint());
		double disTopRight = Vector2d.findDistanceBetweenTwoVector2ds(r.topRightPoint, s.getReferencePoint());
		double disBottomLeft = Vector2d.findDistanceBetweenTwoVector2ds(r.bottomLeftPoint, s.getReferencePoint());
		double disBottomRight = Vector2d.findDistanceBetweenTwoVector2ds(r.bottomRightPoint, s.getReferencePoint());
		if(s instanceof Circle){
			Circle c = (Circle)s;
		if(disTopLeft <= c.getRadius()+10){
			return r.topLeftPoint;
		}else if(disTopRight <= c.getRadius()+10){
			return r.topRightPoint;
		}else if(disBottomRight <= c.getRadius()+10){
			return r.bottomRightPoint;
		}else if(disBottomLeft <= c.getRadius()+10){
			return r.bottomLeftPoint;
		}else{
			return null;
		}
		}else if(s instanceof Rectangle){
		//todo	
		}
		return null;
	}
	
	
	public boolean checkIfRectangleIsWithinCollisionRadius(Rectangle r, double collisionRadius){
		ArrayList<Vector2d> topSide = new ArrayList<Vector2d>();
		ArrayList<Vector2d> bottomSide = new ArrayList<Vector2d>();
		ArrayList<Vector2d> leftSide = new ArrayList<Vector2d>();
		ArrayList<Vector2d> rightSide = new ArrayList<Vector2d>();
		topSide.add(r.topLeftPoint);
		topSide.add(r.topRightPoint);
		
		rightSide.add(r.topRightPoint);
		rightSide.add(r.bottomRightPoint);
		bottomSide.add(r.bottomLeftPoint);
		bottomSide.add(r.bottomRightPoint);
		leftSide.add(r.topLeftPoint);
		leftSide.add(r.bottomLeftPoint);
		if(Vector2d.findDistanceBetweenTwoVector2ds(this.getColliderShape().referencePoint,r.referencePoint) <= collisionRadius)
			return true;
		if(Vector2d.findDistanceBetweenTwoVector2ds(this.getColliderShape().referencePoint,r.topLeftPoint) <= collisionRadius)
				return true;
		if(	Vector2d.findDistanceBetweenTwoVector2ds(this.getColliderShape().referencePoint,r.topRightPoint) <= collisionRadius)
			return true;
		if(Vector2d.findDistanceBetweenTwoVector2ds(this.getColliderShape().referencePoint,r.bottomRightPoint) <= collisionRadius)
			return true;
		if(Vector2d.findDistanceBetweenTwoVector2ds(this.getColliderShape().referencePoint,r.bottomLeftPoint) <= collisionRadius)
			return true;
		if(NewMath.calculateDistanceOfPointToLine(this.getColliderShape().referencePoint,topSide) <= collisionRadius)
			return true;
		if(NewMath.calculateDistanceOfPointToLine(this.getColliderShape().referencePoint,bottomSide) <= collisionRadius)
			return true;
		if(NewMath.calculateDistanceOfPointToLine(this.getColliderShape().referencePoint,leftSide) <= collisionRadius)
			return true;
		if(NewMath.calculateDistanceOfPointToLine(this.getColliderShape().referencePoint,rightSide) <= collisionRadius)
			return true;
		return false;
	}
	public boolean checkIfCircleIsWithinCollisionRadius(Circle c, double collisionRadius){
		if(Vector2d.findDistanceBetweenTwoVector2ds(c.referencePoint, this.getColliderShape().referencePoint) <= collisionRadius)
			return true;
		if(Vector2d.findDistanceBetweenTwoVector2ds(c.referencePoint, this.getColliderShape().referencePoint)-c.getRadius() <= collisionRadius)
			return true;
		return false;
	}
	public boolean checkIfColliderIsWithInCollisionRadius(Collider other, double collisionRadius){
		if(other.getColliderShape() instanceof Rectangle)
			if(!checkIfRectangleIsWithinCollisionRadius((Rectangle)other.getColliderShape(), collisionRadius))
				return false;
		if(other.getColliderShape() instanceof Circle)
			if(!checkIfCircleIsWithinCollisionRadius((Circle)other.getColliderShape(), collisionRadius))
				return false;
		return true;
	}
	
	public static Vector2d returnsTheCollidingCornerVectorDirection(Rectangle r,Circle c)
	{
		double disTopLeft = Vector2d.findDistanceBetweenTwoVector2ds(r.topLeftPoint, c.getReferencePoint());
		double disTopRight = Vector2d.findDistanceBetweenTwoVector2ds(r.topRightPoint, c.getReferencePoint());
		double disBottomLeft = Vector2d.findDistanceBetweenTwoVector2ds(r.bottomLeftPoint, c.getReferencePoint());
		double disBottomRight = Vector2d.findDistanceBetweenTwoVector2ds(r.bottomRightPoint, c.getReferencePoint());
		if(disTopLeft <= c.getRadius()+10){
			return r.directionTopLeft;
		}else if(disTopRight <= c.getRadius()+10){
			return r.directionTopRight;
		}else if(disBottomRight <= c.getRadius()+10){
			return r.directionBottomRight;
		}else if(disBottomLeft <= c.getRadius()+10){
			return r.directionBottomLeft;
		}else{
			return null;
		}
	}

	/*
	public static void calculateSidePositionCorrection(Collider c1,Collider c2)
	{
		if (c1.getColliderShape() instanceof Circle && c2.getColliderShape() instanceof Rectangle) {
		Circle c = (Circle) c1.getColliderShape();
		Rectangle r = (Rectangle) c2.getColliderShape();
		Vector2d newPos = r.returnSidePositionCorrection(c1);
		if(newPos != null)
		c1.parent.position = newPos; 
		}else if(c2.getColliderShape() instanceof Circle && c1.getColliderShape() instanceof Rectangle) {
			Circle c = (Circle) c2.getColliderShape();
			Rectangle r = (Rectangle) c1.getColliderShape();
			Vector2d newPos = r.returnSidePositionCorrection(c2);
			if(newPos != null)
			c2.parent.position = newPos;
		}
	}
	*/
	public static void calculateCornerPositionCorrection(Collider c1,Collider c2)
	{
		if (c1.getColliderShape() instanceof Circle && c2.getColliderShape() instanceof Rectangle) {
		Circle c = (Circle) c1.getColliderShape();
		Rectangle r = (Rectangle) c2.getColliderShape();
		Vector2d newPos = r.returnCornerPositionCorrection(c1);
		if(newPos != null)
		c1.parent.position = newPos; 
		}else if(c2.getColliderShape() instanceof Circle && c1.getColliderShape() instanceof Rectangle) {
			Circle c = (Circle) c2.getColliderShape();
			Rectangle r = (Rectangle) c1.getColliderShape();
			Vector2d newPos = r.returnCornerPositionCorrection(c2);
			if(newPos != null)
			c2.parent.position = newPos;
		}
	}
	
//	public static Vector2d detectCornerCollision(Collider c1, Collider c2){
//		Vector2d hit = null;
//		if (c1.getColliderShape() instanceof Circle && c2.getColliderShape() instanceof Rectangle) {
//			Circle c = (Circle) c1.getColliderShape();
//			Rectangle r = (Rectangle) c2.getColliderShape();
//					
//			hit = returnsTheCollidingCornerVectorDirection(r, c);
//			return hit;
//		} else if (c2.getColliderShape() instanceof Circle && c1.getColliderShape() instanceof Rectangle) {
//			Circle c = (Circle) c2.getColliderShape();
//			Rectangle r = (Rectangle) c1.getColliderShape();	
//			hit = returnsTheCollidingCornerVectorDirection(r, c);
//			return hit;
//		}
//		return hit;
//	}
	public static Vector2d detectCornerCollision(Collider circle, Collider rectangle){
		Vector2d hit = null;
			Circle c = (Circle) circle.getColliderShape();
			Rectangle r = (Rectangle) rectangle.getColliderShape();
					
			hit = returnsTheCollidingCornerVectorDirection(r, c);
		return hit;
	}
	/*
	public double calculateDotOfLineAndShapeMovement(Collider c1,ArrayList<Vector2d> l)
	{
		
	}
	public static double calculateDotOfShapeMovementAndShapePosition(Shape s1,Shape s2){
		// (s2 instanceof Rectangle || s2 instanceof Circle){
			if(s1 instanceof Rectangle && s2 instanceof Circle){
				Circle c = (Circle)s2;
				Rectangle r = (Rectangle)s1;
				
			}
	}*/
	
	public static void calculateCollisionOfTwoCircles(Collider c1, Collider c2){
		GameObject firstCircle = (GameObject)c1.parent;
		GameObject secondCircle = (GameObject)c2.parent;
	    Vector2d tempFirstVelocity = new Vector2d(firstCircle.velocity);
	    Vector2d tempSecondVelocity = new Vector2d(secondCircle.velocity);
	    if(GameManager.realisticCollisions){
	    	double mass1 = firstCircle.mass;
	    	double mass2 = secondCircle.mass;
//	    	System.err.println("vel1"+tempFirstVelocity);
//	    	System.err.println("vel2"+tempSecondVelocity);
//	    	double newVelX1 = (firstCircle.velocity.getX() * (firstCircle.mass - secondCircle.mass) + (2 * secondCircle.mass * secondCircle.velocity.getX())) / (firstCircle.mass + secondCircle.mass);
//	    	double newVelY1 = (firstCircle.velocity.getY() * (firstCircle.mass - secondCircle.mass) + (2 * secondCircle.mass * secondCircle.velocity.getY())) / (firstCircle.mass + secondCircle.mass);
//	    	double newVelX2 = (secondCircle.velocity.getX() * (secondCircle.mass - firstCircle.mass) + (2 * firstCircle.mass * firstCircle.velocity.getX())) / (firstCircle.mass + secondCircle.mass);
//	    	double newVelY2 = (secondCircle.velocity.getY() * (secondCircle.mass - firstCircle.mass) + (2 * firstCircle.mass * firstCircle.velocity.getY())) / (firstCircle.mass + secondCircle.mass);
	    	double newVelX1 = (tempFirstVelocity.getX() * (firstCircle.mass - secondCircle.mass) + (2 * secondCircle.mass * tempSecondVelocity.getX())) / (firstCircle.mass + secondCircle.mass);
	    	double newVelY1 = (tempFirstVelocity.getY() * (firstCircle.mass - secondCircle.mass) + (2 * secondCircle.mass * tempSecondVelocity.getY())) / (firstCircle.mass + secondCircle.mass);
	    	double newVelX2 = (tempSecondVelocity.getX() * (secondCircle.mass - firstCircle.mass) + (2 * firstCircle.mass * tempFirstVelocity.getX())) / (firstCircle.mass + secondCircle.mass);
	    	double newVelY2 = (tempSecondVelocity.getY() * (secondCircle.mass - firstCircle.mass) + (2 * firstCircle.mass * tempFirstVelocity.getY())) / (firstCircle.mass + secondCircle.mass);
	    	Vector2d newVelFirst = new Vector2d(newVelX1,newVelY1);
	    	Vector2d newVelSecond = new Vector2d(newVelX2,newVelY2);
//	    	System.err.println("newVelX1"+newVelX1+"newVelY1"+newVelY1);
//	    	System.err.println("newVelX2"+newVelX2+"newVelY2"+newVelY2);
//	    	System.err.println("newVel2"+newVelSecond);
	    	if(firstCircle.isMovable)
	    	firstCircle.setVelocity(newVelFirst);
	    	if(secondCircle.isMovable)
	    	secondCircle.setVelocity(newVelSecond);
	    	return;
	    }
	    Circle circle1 = (Circle)c1.getColliderShape();
	    Circle circle2 = (Circle)c2.getColliderShape();
	    //double dis = Vector2d.findDistanceBetweenTwoVector2ds(circle1.referencePoint, circle2.referencePoint);
	   // double correction = dis-(circle1.getRadius()/2+circle2.getRadius()/2);
	    if(firstCircle.isMovable)
	    firstCircle.setVelocity(tempSecondVelocity);
	    if(secondCircle.isMovable)
	    secondCircle.setVelocity(tempFirstVelocity);
	}

	
	public static boolean areCircleAndRectangleGoingToCollide(Rectangle r,Circle c,Vector2d movevec){
		//System.out.println(r.topSide);
		ArrayList<Vector2d> topSide = new ArrayList<Vector2d>();
		topSide.add(r.topLeftPoint);
		topSide.add(r.topRightPoint);
		ArrayList<Vector2d> bottomSide = new ArrayList<Vector2d>();
		bottomSide.add(r.bottomLeftPoint);
		bottomSide.add(r.bottomRightPoint);
		ArrayList<Vector2d> leftSide = new ArrayList<Vector2d>();
		leftSide.add(r.topLeftPoint);
		leftSide.add(r.bottomLeftPoint);
		ArrayList<Vector2d> rightSide = new ArrayList<Vector2d>();
		rightSide.add(r.topRightPoint);
		rightSide.add(r.bottomRightPoint);
		boolean collideWithTop = areCircleAndLineGoingToCollide(topSide, c, movevec);
		boolean collideWithLeft = areCircleAndLineGoingToCollide(leftSide, c, movevec);
		boolean collideWithBottom = areCircleAndLineGoingToCollide(bottomSide, c, movevec);
		boolean collideWithRight = areCircleAndLineGoingToCollide(rightSide, c, movevec);
//		boolean collideTopLeft = areCircleAndCornerGoingToCollide(c, r.topLeftPoint,movevec);
//		boolean collideTopRight = areCircleAndCornerGoingToCollide(c, r.topRightPoint,movevec);
//		boolean collideBottomLeft = areCircleAndCornerGoingToCollide(c, r.bottomLeftPoint,movevec);
//		boolean collideBottomRight = areCircleAndCornerGoingToCollide(c, r.bottomRightPoint,movevec);
		//collideTopLeft || collideTopRight || collideBottomLeft || collideBottomRight || 
		if(collideWithTop || collideWithBottom || collideWithLeft || collideWithRight){
			return true;
		}
		return false;
	}
	
	
	public static boolean areCircleAndCornerGoingToCollide(Circle A, Vector2d cornerPoint,Vector2d movevec){

		// Early Escape test: if the length of the movevec is less
		// than distance between the centers of these circles minus 
		// their radii, there's no way they can hit.
		
		double dist = Vector2d.findDistanceBetweenTwoVector2ds(cornerPoint, A.referencePoint);
		double sumRadii = (A.getRadius());
		dist -= sumRadii;
		if(movevec.length() < dist){
		  return false;
		}

		// Normalize the movevec
		Vector2d N = movevec.normalized();

		// Find C, the vector from the center of the moving 
		// circle A to the center of B
		
		Vector2d C = Vector2d.sub(cornerPoint,A.referencePoint);

		// D = N . C = ||C|| * cos(angle between N and C)
		double D = Vector2d.dotProduct(N, C);

		// Another early escape: Make sure that A is moving 
		// towards B! If the dot product between the movevec and 
		// B.center - A.center is less that or equal to 0, 
		// A isn't isn't moving towards B
		if(D <= 0){
		  return false;
		}
		// Find the length of the vector C
		double lengthC = C.length();

		double F = (lengthC * lengthC) - (D * D);

		// Escape test: if the closest that A will get to B 
		// is more than the sum of their radii, there's no 
		// way they are going collide
		double sumRadiiSquared = sumRadii * sumRadii;
		if(F >= sumRadiiSquared){
		  return false;
		}

		// We now have F and sumRadii, two sides of a right triangle. 
		// Use these to find the third side, sqrt(T)
		double T = sumRadiiSquared - F;

		// If there is no such right triangle with sides length of 
		// sumRadii and sqrt(f), T will probably be less than 0. 
		// Better to check now than perform a square root of a 
		// negative number. 
		if(T < 0){
		  return false;
		}

		// Therefore the distance the circle has to travel along 
		// movevec is D - sqrt(T)
		double distance = D - Math.sqrt(T);

		// Get the magnitude of the movement vector
		double mag = movevec.length();

		// Finally, make sure that the distance A has to move 
		// to touch B is not greater than the magnitude of the 
		// movement vector. 
		if(mag < distance){
		  return false;
		}

		// Set the length of the movevec so that the circles will just 
		// touch
		//movevec.normalize();
		//movevec.times(distance);
		movevec = Vector2d.mul(distance,movevec.normalized());
		return true;
		
	}
	public static boolean areCircleAndLineGoingToCollide(ArrayList<Vector2d> line, Circle c, Vector2d movevec){
		// Early Escape test: if the length of the movevec is less
		// than distance between the centers of these circles minus 
		// their radii, there's no way they can hit.
		Circle A = c;
		double dist = NewMath.distanceBetweenPointAndLine(A.referencePoint,line);
		//double dist = Vector2d.findDistanceBetweenTwoVector2ds(B.referencePoint, A.referencePoint);
		double sumRadii = (A.getRadius());
		dist -= sumRadii;
		if(movevec.length() < dist){
		  return false;
		}

		// Normalize the movevec
		Vector2d N = movevec.normalized();

		// the point on the line that is the closest to the center of circle A
		Vector2d distancePoint = Vector2d.findPointThatNormalOfAPointToALineMeet(line.get(0), line.get(line.size()-1), A.referencePoint);
		// Find C, the vector from the center of the moving 
		// circle A to the center of B
		Vector2d C = Vector2d.sub(distancePoint,A.referencePoint);

		// D = N . C = ||C|| * cos(angle between N and C)
		double D = Vector2d.dotProduct(N, C);

		// Another early escape: Make sure that A is moving 
		// towards B! If the dot product between the movevec and 
		// B.center - A.center is less that or equal to 0, 
		// A isn't isn't moving towards B
		if(D <= 0){
		  return false;
		}
		// Find the length of the vector C
		double lengthC = C.length();

		double F = (lengthC * lengthC) - (D * D);

		// Escape test: if the closest that A will get to B 
		// is more than the sum of their radii, there's no 
		// way they are going collide
		double sumRadiiSquared = sumRadii * sumRadii;
		if(F >= sumRadiiSquared){
		  return false;
		}

		// We now have F and sumRadii, two sides of a right triangle. 
		// Use these to find the third side, sqrt(T)
		double T = sumRadiiSquared - F;

		// If there is no such right triangle with sides length of 
		// sumRadii and sqrt(f), T will probably be less than 0. 
		// Better to check now than perform a square root of a 
		// negative number. 
		if(T < 0){
		  return false;
		}

		// Therefore the distance the circle has to travel along 
		// movevec is D - sqrt(T)
		double distance = D - Math.sqrt(T);

		// Get the magnitude of the movement vector
		double mag = movevec.length();

		// Finally, make sure that the distance A has to move 
		// to touch B is not greater than the magnitude of the 
		// movement vector. 
		if(mag < distance){
		  return false;
		}

		// Set the length of the movevec so that the circles will just 
		// touch
		//movevec.normalize();
		//movevec.times(distance);
		movevec = Vector2d.mul(distance,movevec.normalized());
		return true;
	}
	//returns vector2d.zero() if it is a corner;
	public static boolean areCirclesGoingToCollide(Circle A, Circle B,Vector2d movevec){

		// Early Escape test: if the length of the movevec is less
		// than distance between the centers of these circles minus 
		// their radii, there's no way they can hit.
		
		double dist = Vector2d.findDistanceBetweenTwoVector2ds(B.referencePoint, A.referencePoint);
		double sumRadii = (B.getRadius() + A.getRadius());
		dist -= sumRadii;
		if(movevec.length() < dist){
		  return false;
		}

		// Normalize the movevec
		Vector2d N = movevec.normalized();

		// Find C, the vector from the center of the moving 
		// circle A to the center of B
		
		Vector2d C = Vector2d.sub(B.referencePoint,A.referencePoint);

		// D = N . C = ||C|| * cos(angle between N and C)
		double D = Vector2d.dotProduct(N, C);

		// Another early escape: Make sure that A is moving 
		// towards B! If the dot product between the movevec and 
		// B.center - A.center is less that or equal to 0, 
		// A isn't isn't moving towards B
		if(D <= 0){
		  return false;
		}
		// Find the length of the vector C
		double lengthC = C.length();

		double F = (lengthC * lengthC) - (D * D);

		// Escape test: if the closest that A will get to B 
		// is more than the sum of their radii, there's no 
		// way they are going collide
		double sumRadiiSquared = sumRadii * sumRadii;
		if(F >= sumRadiiSquared){
		  return false;
		}

		// We now have F and sumRadii, two sides of a right triangle. 
		// Use these to find the third side, sqrt(T)
		double T = sumRadiiSquared - F;

		// If there is no such right triangle with sides length of 
		// sumRadii and sqrt(f), T will probably be less than 0. 
		// Better to check now than perform a square root of a 
		// negative number. 
		if(T < 0){
		  return false;
		}

		// Therefore the distance the circle has to travel along 
		// movevec is D - sqrt(T)
		double distance = D - Math.sqrt(T);

		// Get the magnitude of the movement vector
		double mag = movevec.length();

		// Finally, make sure that the distance A has to move 
		// to touch B is not greater than the magnitude of the 
		// movement vector. 
		if(mag < distance){
		  return false;
		}

		// Set the length of the movevec so that the circles will just 
		// touch
		//movevec.normalize();
		//movevec.times(distance);
		movevec = Vector2d.mul(distance,movevec.normalized());
		return true;
		
	}
//	public static Vector2d detectCollision(Collider circle, Collider rectangle) {
//		Vector2d hit = null;
//		if (circle.getColliderShape() instanceof Circle && rectangle.getColliderShape() instanceof Rectangle) {
//			Circle c = (Circle) circle.getColliderShape();
//			Rectangle r = (Rectangle) rectangle.getColliderShape(); 
//					hit = checkWhichSideOfRectCircleIsOn(r, c);
//			return hit;
//		} else if (rectangle.getColliderShape() instanceof Circle && circle.getColliderShape() instanceof Rectangle) {
//			Circle c = (Circle) rectangle.getColliderShape();
//			Rectangle r = (Rectangle) circle.getColliderShape();
//			hit = checkWhichSideOfRectCircleIsOn(r, c);
//			return hit;
//		}
//		//return checkWhichSideOfShapeShapeIsOn(c1.getColliderShape(), c2.getColliderShape());
//		return hit;
//	}
	public static Vector2d detectCollision(Collider circle, Collider rectangle) {
		Vector2d hit = null;
			Circle c = (Circle) circle.getColliderShape();
			Rectangle r = (Rectangle) rectangle.getColliderShape(); 
					hit = checkWhichSideOfRectCircleIsOn(r, c);
			return hit;
	}
	/**
	 * this method draws the collider onto the screen as long as drawCollider ==
	 * true
	 * 
	 * @param g
	 */
	public void drawCollider(Graphics g) {
		g.setColor(Color.green);
		Graphics2D g2 = (Graphics2D) g;
		if (drawCollider) {
			// draw referencePoint
			Vector2d newPos = collider.referencePoint;
			g2.fill(new Ellipse2D.Double(newPos.getX()-2.5,newPos.getY()-2.5,5,5));
			if(collider instanceof Circle){
//				Vector2d newPos = ((Circle) getColliderShape()).convertPositionToWorkWithJavaGraphics();
				//Vector2d newPos = collider.referencePoint;
				g2.draw(new Ellipse2D.Double(newPos.getX()-collider.getShapeWidth()/2,newPos.getY()-collider.getShapeHeight()/2,collider.getShapeWidth(),collider.getShapeHeight()));
				
				return;
			}
			ArrayList<Vector2d> newPoints = null;
			newPoints = collider.points;
			if(newPoints == null || newPoints.isEmpty())
				return;
			for (int i = 0; i < newPoints.size() - 1; i++) {
				if (i % 2 == 0)
					g.setColor(Color.green);
//				else
//					g.setColor(Color.);
				int x1 = (int) newPoints.get(i).getX();
				int y1 = (int) newPoints.get(i).getY();
				int x2 = (int) newPoints.get(i + 1).getX();
				int y2 = (int) newPoints.get(i + 1).getY();
				
				g.drawLine(x1, y1, x2, y2);
			}

			if (collider instanceof Rectangle) {
				//g2.rotate(collider.rotationAngle,collider.referencePoint.getX(),collider.referencePoint.getY());
				g2.drawLine((int) collider.getPoints().get(0).getX(), (int) collider.getPoints().get(0).getY(),
						(int) collider.getPoints().get(collider.getPoints().size() - 1).getX(),
						(int) collider.getPoints().get(collider.getPoints().size() - 1).getY());
			}
			
		}
	}

	public void updatePosition(Vector2d velocity) {
		if (getColliderShape() instanceof Circle) {
			((Circle) getColliderShape()).moveReferencePoint(velocity);
			parent.setPosition(collider.referencePoint);
		} else if (getColliderShape() instanceof Rectangle) {
			((Rectangle) getColliderShape()).moveReferencePoint(velocity);
			parent.setPosition(collider.referencePoint);
		}
	}

	public void moveToPosition(Vector2d position){
		if (getColliderShape() instanceof Circle) {
			position = Vector2d.sub(position, collider.referencePoint);
			((Circle) getColliderShape()).moveReferencePoint(position);
			parent.setPosition(collider.referencePoint);
		} else if (getColliderShape() instanceof Rectangle) {
			position = Vector2d.sub(position, collider.referencePoint);
			((Rectangle) getColliderShape()).moveReferencePoint(position);
			parent.setPosition(collider.referencePoint);
		}
	}
	
	/**
	 * @return the drawCollider
	 */
	public boolean DrawCollider() {
		return drawCollider;
	}

	/**
	 * @param drawCollider
	 *            the drawCollider to set
	 */
	public void setDrawCollider(boolean drawCollider) {
		this.drawCollider = drawCollider;
	}

	/**
	 * @return the colliderID
	 */
	public int getColliderID() {
		return colliderID;
	}

	/**
	 * @param colliderID
	 *            the colliderID to set
	 */
	public void setColliderID(int colliderID) {
		this.colliderID = colliderID;
	}

	/**
	 * @return the parent
	 */
	public PhysicsObject getParent() {
		return parent;
	}

	public void setParent(PhysicsObject physicsObject) {
		this.parent = physicsObject;
	}

	/**
	 * @return the isCollidable
	 */
	public boolean isCollidable() {
		return isCollidable;
	}

	/**
	 * @param isCollidable
	 *            the isCollidable to set
	 */
	public void setCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}

}
