package gameMechanics;

import math.Vector2d;

@Deprecated
public class CircleRectCollision extends Collision{

	boolean isCornerCollision = false;
	public CircleRectCollision(Collider c1, Collider c2, Vector2d hit,boolean isCornerCollision) {
		super(c1, c2, hit);
		this.isCornerCollision = isCornerCollision;
	}

	public Collider getCircleCollider(){
		if(c1.getColliderShape() instanceof Circle)
			return c1;
		else
			return c2;		
	}
	
	public Collider getRectCollider(){
		if(c1.getColliderShape() instanceof Rectangle)
			return c1;
		else
			return c2;
	}
}
