package gameMechanics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import math.*;

/**
 * @author Ethan
 * This represents a Shape in my engine, using an {@link ArrayList} of {@link Vector2d}
 */
public class Shape implements Serializable{
	
	private static final long serialVersionUID = -5247811614567004936L;
	protected ArrayList<Vector2d> points;
	protected Vector2d referencePoint;
	protected double rotationAngle = 0;
	private double shapeHeight;
	private double shapeWidth;
	
	public Shape(ArrayList<Vector2d> points, Vector2d referencePoint){
		this.referencePoint = new Vector2d(referencePoint);
		this.referencePoint = new Vector2d(referencePoint.getX(),referencePoint.getY());
		this.points = new ArrayList<Vector2d>();
		for (Vector2d vector2d : points) {
			this.points.add(vector2d);
		}
		calculateHeightAndWidth();
	}
	
	public Shape(Vector2d referencePoint, double shapeWidth, double shapeHeight){
		this.referencePoint = referencePoint;
		this.shapeHeight = shapeHeight;
		this.shapeWidth = shapeWidth;
	}
	
	public Shape(Shape colliderShape) {
		this.referencePoint = new Vector2d(colliderShape.referencePoint);
		points = new ArrayList<Vector2d>();
		if(colliderShape.points != null){
		for (int i = 0; i< colliderShape.points.size();i++) {
			this.points.add(new Vector2d(colliderShape.points.get(i).getX(),colliderShape.points.get(i).getY()));
		}
		}
		this.shapeHeight = colliderShape.shapeHeight;
		this.shapeWidth = colliderShape.shapeWidth;
	}

	/**
	 * calculates the absolute height and with of the {@link Shape}
	 */
	public void calculateHeightAndWidth(){
		double[] ys = new double[points.size()];
		for(int i = 0; i<points.size();i++){
			ys[i] = points.get(i).getY();
		}
		double maxY = ys[0];
		for (int i = 1; i < ys.length; i++) {
				maxY = Math.max(maxY,ys[i]);	
			}
		double minY = ys[0];
		for (int i = 1; i < ys.length; i++) {
			minY = Math.min(minY,ys[i]);	
		}
		double height = maxY-minY;
		shapeHeight = height;
		
		double[] xs = new double[points.size()];
		for(int i = 0; i<points.size();i++){
			xs[i] = points.get(i).getX();
		}
		double maxX = xs[0];
		for (int i = 1; i < xs.length; i++) {
				maxX = Math.max(maxX,xs[i]);	
			}
		double minX = xs[0];
		for (int i = 1; i < xs.length; i++) {
			minX = Math.min(minX,xs[i]);	
		}
		double width = maxX-minX;
		shapeWidth = width;
	}
	
	/**
	 * @return {@link Shape#rotationAngle}
	 */
	public double getRotationAngle(){
		return rotationAngle;
	}
	
	/** sets {@link Shape#rotationAngle} to angle
	 * @param angle the angle to set in degrees.
	 */
	public void setRotationAngle(double angle){
		rotationAngle = angle;
	}

	/**
	 * rotates the shape around its {@link Shape#referencePoint}
	 * @param angle the amount to rotate in degrees
	 */
	public void rotate(double angle){
		double prevAngle = this.rotationAngle;
		//prevAngle = 0;
		prevAngle = Math.toRadians(-prevAngle);
		//double deltaAngle = -prevAngle;
		//System.out.println("deltaAng"+deltaAngle);
		for (int i = 0; i < points.size(); i++) {
			Vector2d newPoint = Vector2d.rotatePointAroundAnotherPoint(points.get(i),referencePoint,prevAngle);
			points.get(i).setX(newPoint.getX());
			points.get(i).setY(newPoint.getY());
		}
		this.rotationAngle = angle;
		angle = Math.toRadians(angle);
		for (int i = 0; i < points.size(); i++) {
			Vector2d newPoint = Vector2d.rotatePointAroundAnotherPoint(points.get(i),referencePoint,angle);
			points.get(i).setX(newPoint.getX());
			points.get(i).setY(newPoint.getY());
		}
	}

	/**
	 * @return {@link Shape#referencePoint}
	 */
	public Vector2d getPositionRelativeToJavaGraphics(){
		return referencePoint;
	}
	
	/**
	 * @return {@link Shape#referencePoint}
	 */	
	public Vector2d convertPositionToWorkWithJavaGraphics(){
		return referencePoint;
	}

	/**
	 * @return {@link Shape#points}
	 */
	public ArrayList<Vector2d> getPoints(){
		return points;
	}
	
	/**
	 * @param newPoints sets {@link Shape#points} to newPoints
	 */
	public void setPoints(ArrayList<Vector2d> newPoints){
		points = newPoints;
	}
	
	/**
	 * @param newReferencePoint sets {@link #referencePoint}
	 */
	public void setReferencePoint(Vector2d newReferencePoint){
		this.referencePoint = newReferencePoint;
	}
	
	/**
	 * @return {@link #referencePoint}
	 */
	public Vector2d getReferencePoint(){
		return referencePoint;
	}
	
	/**
	 * this moves {@link #referencePoint} to newReferencePoint. Also, according to the {@link Shape} it moves all the points
	 * according to the newReferencePoint
	 * @param newReferencePoint the {@link Vector2d} position to use to move
	 */	
	public void moveReferencePoint(Vector2d newReferencePoint){

		for(Vector2d point : points){
				point = Vector2d.add(point,newReferencePoint);
			}
	}
		
	/**
	 * @param newCirclePosition the newCirclePosition to add to the {@link Vector2d} position
	 * @return ArrayList<{@link Vector2d}> points that were moved according to newCirclePosition
	 */
	public ArrayList<Vector2d> returnPointsWithNewReference(Vector2d newCirclePosition){
		ArrayList<Vector2d> newPoints = new ArrayList<Vector2d>();
		newPoints.addAll(points);
		for (Vector2d newPoint : newPoints) {
			newPoint = Vector2d.add(Vector2d.sub(newPoint,referencePoint), newCirclePosition);
		}
		return newPoints;
	}

	/**
	 * @return the {@link #shapeHeight}
	 */
	public double getShapeHeight() {
		return shapeHeight;
	}

	/**
	 * @return the {@link #shapeWidth}
	 */
	public double getShapeWidth() {
		return shapeWidth;
	}

	/**
	 * @param {@link #shapeHeight} the {@link #shapeHeight} to set
	 */
	public void setShapeHeight(double shapeHeight) {
		this.shapeHeight = shapeHeight;
	}

	/**
	 * @param #shapeWidth the {@link #shapeWidth} to set
	 */
	public void setShapeWidth(double shapeWidth) {
		this.shapeWidth = shapeWidth;
	}

}