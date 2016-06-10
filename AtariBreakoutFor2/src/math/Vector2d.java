package math;

import java.io.Serializable;

import gameMechanics.Circle;
import gameMechanics.Collider;
import gameMechanics.Rectangle;

/**  This is represents two values, x and y. it can be used for position, velocity etc.
 * @author Ethan
 *
 */
public class Vector2d implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2665828588100820303L;
	private double x,y;
	//public static final Vector2d ZERO = new Vector2d(0,0);
	public Vector2d(double x,double y){
		this.setX(x);
		this.setY(y);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[X= "+x+" , Y= "+y+" ]";
	}
	/**
	 * @param v1
	 * @param v2
	 * @return the distance between two vectors v1 & v2
	 */
	public static double findDistanceBetweenTwoVector2ds(Vector2d v1,Vector2d v2){
		double d = Math.sqrt(Math.pow(v1.getX()-v2.getX(),2)+Math.pow(v1.getY()-v2.getY(),2));
		return d;
	}
	
	/**
	 * transition between two Vector2d values.
	 * @param point1 the starting Vector2d
	 * @param point2 the ending Vector2d
	 * @param the amount to transition
	 * @return the Vector2d transitioned alpha amount from point1 to point2
	 */
	public Vector2d lerp(Vector2d point1, Vector2d point2, float alpha)
	{
	    return add(point1,mul(alpha,(sub(point2,point1))));
	}
	
	/** 
	 * @param position the center position of the {@link gameMechanics.Shape}
	 * @param collider the Collider that its position we're trying to convert
	 * @return the corrected position from center to top-left corner (like java graphics requires)
	 */
	public Vector2d convertToJavaGraphicsPosition(Vector2d position,Collider collider){
		if(collider.getColliderShape() instanceof Circle){
		double x = position.getX()+((Circle)collider.getColliderShape()).getRadius();
		double y = position.getY()+((Circle)collider.getColliderShape()).getRadius();
		return new Vector2d(x,y);
		}else if(collider.getColliderShape() instanceof Rectangle){

			double x = position.getX()+((Rectangle)collider.getColliderShape()).getLengthOfSideB()/2;
			double y = position.getY()+((Rectangle)collider.getColliderShape()).getLengthOfSideA()/2;
			return new Vector2d(x,y);
		}
		return position;
	}
	
	@Deprecated
	public boolean checkIfPointIsOnLeftSideOfLine(Vector2d startPoint,Vector2d endPoint,Vector2d p){
		
		double n = Math.sin((endPoint.getX() - startPoint.getX()) * (p.getY() - startPoint.getY()) - (endPoint.y - startPoint.y) * (p.x - startPoint.x));
		return (n < 0);
	}
	
	/** 
	 * @param startPoint startPoint of line
	 * @param endPoint endPoint of line
	 * @param p start point of normal
	 * @return the point where the normal of a point to a line meet
	 */
	public static Vector2d findPointThatNormalOfAPointToALineMeet(Vector2d startPoint,Vector2d endPoint,Vector2d p){
		Vector2d d = sub(endPoint,startPoint).normalized();
			Vector2d x = add(mul(dotProduct(sub(p,startPoint),d),d),startPoint);
			return x;
	}

	/** 
	 * @param normal the normal direction vector that is used to reflect.
	 * @return the reflected vector direction according to a normal direction vector.
	 */
	public Vector2d reflect(Vector2d normal){
		return add(mul(-2*dotProduct(normal.normalized(), this),normal.normalized()), this);
	}
	
	/**
	 * @return the normalized vector: i.e. the vector with it's x,y parameters each divided by its
	 * length.
	 */
	public Vector2d normalized(){
		double length = length();
		if(length == 0)
			return Vector2d.zero();
		double x = this.x / length;
		double y = this.y / length;
		
		return new Vector2d(x,y);
	}
	
	/**
	 * @return the length of the vector i.e. its magnitude.
	 */
	public double length(){
		return Math.sqrt(this.x*this.x+this.y*this.y);
	}
	
	/**
	 * @param v the vector to rotate
	 * @param theta the angle to rotate in radians
	 * @return the rotated vector
	 */
	public static Vector2d rotate(Vector2d v, double theta){
		//theta = -theta;
		double x2 = Math.cos(theta)*v.getX() - Math.sin(theta)*v.getY();
		double y2 = Math.sin(theta)*v.getX() + Math.cos(theta)*v.getY();
		return new Vector2d(x2,y2);
	}
	
	/**
	 * @param v1 Vector2d
	 * @param v2 Vector2d
	 * @return the dot product between vectors v1 and v2
	 */
	public static double dotProduct(Vector2d v1, Vector2d v2){
		return v1.x*v2.x+v1.y*v2.y;
	}
	
	/**
	 * 
	 * @param v Vector2d
	 * @return return the vector that is perpendicular to this vector 
	 */
	public static Vector2d getNormalOfVector(Vector2d v){
		return new Vector2d(-v.y,v.x);
	}
	
	/**
	 * @return this vector's length value
	 */
	public double getLength(){
		return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
	}
	
	/**
	 * @param p1 the point to rotate.
	 * @param center the center point to rotate around.
	 * @return rotates a point to 0 degrees around the center point.
	 */
	public static Vector2d resetRotationOfPointAroundAnotherPoint(Vector2d p1,Vector2d center){
		//TRANSLATE TO ORIGIN
				double x1 = p1.getX() - center.x;
				double y1 = p1.getY() - center.y;

				//APPLY ROTATION
				double temp_x1 = x1 * Math.cos(0) - y1 * Math.sin(0);
				double temp_y1 = x1 * Math.sin(0) + y1 * Math.cos(0);

				//TRANSLATE BACK
				return new Vector2d(temp_x1 + center.x, temp_y1 + center.y);
	}
	
	
	/**
	 * @param p1 the point to rotate.
	 * @param center the point to rotate p1 around
	 * @param angle rotation angle in radians
	 * @return rotates a point according to angle around the center point.
	 */
	public static Vector2d rotatePointAroundAnotherPoint(Vector2d p1,Vector2d center,double angle){
		//angle = -angle;
		
		//TRANSLATE TO ORIGIN
		double x1 = p1.getX() - center.x;
		double y1 = p1.getY() - center.y;

		//APPLY ROTATION
		double temp_x1 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double temp_y1 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		//TRANSLATE BACK
		return new Vector2d(temp_x1 + center.x, temp_y1 + center.y);
		//return new Vector2d(temp_x1, temp_y1);
		
		/*
		double newX = center.x + (p1.x-center.x)*Math.cos(angle) - (p1.y-center.y)*Math.sin(angle);

		double newY = center.y + (p1.x-center.x)*Math.sin(angle) + (p1.y-center.y)*Math.cos(angle);
		return new Vector2d(newX,newY);*/
	}

	/**
	 * 
	 * @param v1 Vector2d
	 * @param v2 Vector2d
	 * @return angle in radians between two vectors
	 */
	public static double angleBetweenTwoVector2ds(Vector2d v1, Vector2d v2){
		System.out.println((v1));//*v2.getLength()));
		return Math.acos(dotProduct(v1, v2)/(v1.getLength()*v2.getLength()));
	}
	
	/**
	 * 
	 * @param n double
	 * @param v Vector2d
	 * @return a vector with the values of v times the number n
	 */
	public static Vector2d mul(double n, Vector2d v){
		double x = v.getX(), y = v.getY();
		x*=n;
		y*=n;
		return new Vector2d(x,y);
	}
	
	/**
	 * @param n double
	 * @param v Vector2d
	 * @return a vector with the values of v divided the number n
	 */
	public static Vector2d div(double n, Vector2d v){
		double x = v.getX(), y = v.getY();
		x/=n;
		y/=n;
		return new Vector2d(x,y);
	}
	
	
	public Vector2d(Vector2d newVector){
		this.x = newVector.getX();
		this.y = newVector.getY();
	}
	
	public static Vector2d zero(){
		return new Vector2d(0, 0);
	}
	
	/**
	 * @param v1
	 * @param v2
	 * @return a vector with values that are equal to the added values of each vector by the other
	 */
	public static Vector2d add(Vector2d v1,Vector2d v2){
		int x = (int)v1.getX(), y = (int)v1.getY();
		x+=v2.getX();
		y+=v2.getY();
		return new Vector2d(x,y);
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return a vector with values that are equal to the subtracted values of each vector by the other
	 */
	public static Vector2d sub(Vector2d v1,Vector2d v2){
		double x = v1.getX(), y = v1.getY();
		x-=v2.getX();
		y-=v2.getY();
		return new Vector2d(x,y);
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Vector2d){
			Vector2d other = ((Vector2d)obj);
			if(other.x == this.x && other.y == this.y){
				return true;
			}
		}
		return false;
	}
}
