package gameMechanics;

import java.util.ArrayList;

import math.Vector2d;

public class Circle extends Shape{
	
	private double radius; // the radius of the circle
	private int numOfPoints; // the number of points the circumference has, the more there is the rounder the circle
	
	/**
	 * Constructor for create a circle shape
	 * @param radius the radius of the circle
	 * @param numOfPoints the number of points that the circle has, the more it has the rounder it is
	 * @param circlePosition the center position of the circle
	 */
	public Circle(double radius, int numOfPoints, Vector2d circlePosition){
		super(circlePosition,radius*2,radius*2);
		this.radius = radius;
		this.numOfPoints = numOfPoints;
//		shapeHeight = radius;
//		shapeWidth = radius;
	}
	
	/**
	 * according to the radius and the number of points received, the method return a list of points on the
	 * circumference of the circle
	 * @param radius
	 * @param numOfPoints
	 * @param circlePosition
	 * @return points
	 */
	public static ArrayList<Vector2d> calculateCirclePoints(double radius,int numOfPoints,Vector2d circlePosition){
		ArrayList<Vector2d> points = new ArrayList<Vector2d>();
//		double angleDelta = (2*Math.PI)/numOfPoints;
//		for(int i = 0; i<numOfPoints;i++){
//			double x = circlePosition.getX() + radius*Math.cos(i*angleDelta)/2;
//			double y = circlePosition.getY() + radius*Math.sin(i*angleDelta)/2;
//			Vector2d newPoint = new Vector2d(x,y);
//			points.add(newPoint);
//		}
		return points;
	}
	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub
		super.rotate(angle);
	}
	
	@Override
	public Vector2d getPositionRelativeToJavaGraphics() {
		// TODO Auto-generated method stub
		return Vector2d.add(referencePoint, new Vector2d(-radius, -radius));
	}
	
	@Override
	public Vector2d convertPositionToWorkWithJavaGraphics() {
		// TODO Auto-generated method stub
		return Vector2d.add(referencePoint, new Vector2d(-radius,-radius));
	}
	
	@Override
	public void moveReferencePoint(Vector2d newCirclePosition) {
		referencePoint = Vector2d.add(referencePoint, newCirclePosition);
		//for (Vector2d point : points) {
			//point = Vector2d.add(Vector2d.add(point,newCirclePosition), new Vector2d(radius,radius));
			points = calculateCirclePoints(radius, numOfPoints, referencePoint);
		//}
	}
	
	@Override
	public ArrayList<Vector2d> returnPointsWithNewReference(Vector2d newCirclePosition){
		return calculateCirclePoints(radius, numOfPoints, Vector2d.add(newCirclePosition, new Vector2d(-radius,-radius)));
	}
	
	/**
	 * @return the numOfPoints
	 */
	public int getNumOfPoints() {
		return numOfPoints;
	}
	/**
	 * @param numOfPoints the numOfPoints to set
	 */
	public void setNumOfPoints(int numOfPoints) {
		this.numOfPoints = numOfPoints;
	}
	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
}
