package gameMechanics;

import java.util.ArrayList;

import math.Vector2d;

public class Circle extends Shape{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4127915996887474478L;
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
	}
	
	@Override
	public void rotate(double angle) {
	//	super.rotate(angle);
	}
	
	@Override
	public Vector2d getPositionRelativeToJavaGraphics() {
		return Vector2d.add(referencePoint, new Vector2d(-radius, -radius));
	}
	
	@Override
	public Vector2d convertPositionToWorkWithJavaGraphics() {
		return Vector2d.add(referencePoint, new Vector2d(-radius,-radius));
	}
	
	@Override
	public void moveReferencePoint(Vector2d newCirclePosition) {
		referencePoint = Vector2d.add(referencePoint, newCirclePosition);
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
