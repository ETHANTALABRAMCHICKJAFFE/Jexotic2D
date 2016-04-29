package gameMechanics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import math.*;
public class Shape implements Serializable{
	
	/**
	 * 
	 */
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
	
	public double getRotationAngle(){
		return rotationAngle;
	}
	
	public void setRotationAngle(double angle){
		rotationAngle = angle;
	}

	/**
	 * 
	 * @param angle in degrees
	 */
	public void rotate(double angle){
		this.rotationAngle = angle;
		angle = Math.toRadians(angle);
		for (int i = 0; i < points.size(); i++) {
			Vector2d newPoint = Vector2d.rotatePointAroundAnotherPoint(points.get(i),referencePoint,angle);
			points.get(i).setX(newPoint.getX());
			points.get(i).setY(newPoint.getY());
		}
	}

	public Vector2d getPositionRelativeToJavaGraphics(){
		return referencePoint;
	}
	
	public Vector2d convertPositionToWorkWithJavaGraphics(){
		return referencePoint;
	}

	public ArrayList<Vector2d> getPoints(){
		return points;
	}
	
	public void setPoints(ArrayList<Vector2d> newPoints){
		points = newPoints;
	}
	
	public void setReferencePoint(Vector2d newReferencePoint){
		this.referencePoint = newReferencePoint;
	}
	
	public Vector2d getReferencePoint(){
		return referencePoint;
	}
	
	/**
	 * this moves referencePoint to newReferencePoint. Also, according to the shape it moves all the points
	 * according to the newReferencePoint
	 * @param newReferencePoint
	 */
	
	public void moveReferencePoint(Vector2d newReferencePoint){

		for(Vector2d point : points){
				point = Vector2d.add(point,newReferencePoint);
			}
	}
		
	public ArrayList<Vector2d> returnPointsWithNewReference(Vector2d newCirclePosition){
		ArrayList<Vector2d> newPoints = new ArrayList<Vector2d>();
		newPoints.addAll(points);
		for (Vector2d newPoint : newPoints) {
			newPoint = Vector2d.add(Vector2d.sub(newPoint,referencePoint), newCirclePosition);
		}
		return newPoints;
	}

	/**
	 * @return the shapeHeight
	 */
	public double getShapeHeight() {
		return shapeHeight;
	}

	/**
	 * @return the shapeWidth
	 */
	public double getShapeWidth() {
		return shapeWidth;
	}

	/**
	 * @param shapeHeight the shapeHeight to set
	 */
	public void setShapeHeight(double shapeHeight) {
		this.shapeHeight = shapeHeight;
	}

	/**
	 * @param shapeWidth the shapeWidth to set
	 */
	public void setShapeWidth(double shapeWidth) {
		this.shapeWidth = shapeWidth;
	}

}