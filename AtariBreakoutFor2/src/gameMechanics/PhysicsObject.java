package gameMechanics;

import math.Vector2d;

public class PhysicsObject{
	protected Vector2d position,velocity;
	protected Collider collider;
	protected double mass;
	protected boolean isMovable = true;
	public PhysicsObject(Vector2d position,Vector2d velocity,double mass){
		this.position = new Vector2d(position.getX(), position.getY());
		this.velocity = new Vector2d(velocity.getX(), velocity.getY());
		this.setMass(mass);
	}
	
	/**
	 * @return the position
	 */
	public Vector2d getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2d position) {
		this.position = position;
	}

	/**
	 * @return the velocity
	 */
	public Vector2d getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Vector2d velocity) {
		//if(isMovable)
		this.velocity = velocity;
		//else
			//System.out.println("");
//			System.out.println("notMovable");
	}
	
	public void updateVelocity(PhysicsObject other){
		// todo: momentum: m1*v1 + m2*v2 = m1*u1+m2*u2; m1*u1 = m1*v1 + m2*v2
		double m1 = mass,m2 = other.mass;
		Vector2d v1 = velocity,v2 = other.velocity;
		Vector2d u1 = Vector2d.zero(),u2 = Vector2d.zero();
	}

	/**
	 * @return the collider
	 */
	public Collider getCollider() {
		return collider;
	}

	/**
	 * @param collider the collider to set
	 */
	public void setCollider(Collider collider) {
		collider.setParent(this);
		this.collider = collider;
	}

	/**
	 * @return the mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * @param mass the mass to set
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * 
	 * @param v1
	 * @param m1
	 * @param v2
	 * @param m2
	 * @return
	 */
	public static Vector2d[] findSecondVelocityUsingMomentum(PhysicsObject o1,PhysicsObject o2,Vector2d v1, double m1, Vector2d v2, double m2){
		double vx1 = v1.getX(),vy1 = v1.getY(),vx2 = v2.getX(),vy2 = v2.getY();
		double ux1 = vx1,uy1 = vy1,ux2 = vx2,uy2 = vy2;
		o1.setVelocity(Vector2d.zero());
		o2.setVelocity(Vector2d.zero());
		ux2 = (2*m1*vx1+(m1+m2)*vx2)/(m1+m2);
		System.err.println("ux2"+ux2);
		ux1 = ux2-vx1-vx2;
		System.err.println("ux1"+ux1);
		uy2 = (2*m1*vy1+(m1+m2)*vy2)/(m1+m2);
		uy1 = uy2-vy1-vy2;
		
//		ux1 = (2*m2*vx2+(m1-m2)*vx1)/(m1+m2);
//		System.err.println("ux1 "+ux1);
//		ux2 = vx1-vx2+ux1;
//		System.err.println("ux2"+ux2);
//		uy1 = (2*m2*vy2+(m1-m2)*vy1)/(m1+m2);
//		uy2 = vy1-vy2+uy1;
		
		Vector2d[] vels = {new Vector2d(ux1,uy1), new Vector2d(ux2,uy2)};
		return vels;
	}
	public void findSecondVelocityOfCollisionWithAnotherSurface(Vector2d hitVelocity, Vector2d normalOfSurface){
		if(!isMovable)
			return;
		//double angleBetweenBothVectors = Math.cos();
	//Vector2d v2 = Vector2d.div((Math.cos(angleBetweenBothVectors)*normalOfSurface.getLength()),Vector2d.mul(angleBetweenBothVectors*hitVelocity.getLength(),normalOfSurface));
		double angle = Vector2d.angleBetweenTwoVector2ds(hitVelocity, normalOfSurface);
		if(angle >= Math.PI){
			angle = angle - Math.PI; 
		}
		System.out.println("angle="+angle);
		Vector2d reversedDirectionVelocity = Vector2d.mul(-1, velocity);
		Vector2d v2 = Vector2d.rotate(velocity, angle);
		//velocity = Vector2d.getNormalizeOfVector(velocity);
		//velocity = v2;
		velocity = v2;
	System.out.println("New Velocity= "+velocity.toString());
	}

	/**
	 * @return the isMovable
	 */
	public boolean isMovable() {
		return isMovable;
	}

	/**
	 * @param isMovable the isMovable to set
	 */
	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}
	
	}