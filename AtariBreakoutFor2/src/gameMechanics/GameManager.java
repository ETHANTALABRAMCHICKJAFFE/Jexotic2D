package gameMechanics;

import java.util.ArrayList;
import java.util.List;

import gamePieces.Game;
import gamePieces.GameObject;
import math.Vector2d;

public class GameManager implements Runnable {
	public static ArrayList<Collider> collidersInGame;
	public static int numOfCollidersInGame = 0;
	public static boolean realisticCollisions = false;
	public static ArrayList<Collider> collidingColliders;
	public static final int G = 10;
	public static ArrayList<Collision> collisions;
	public static int numOfCircleCollisions = 0;
	public static QuadTree quad = new QuadTree(0, new java.awt.Rectangle(-1000, -1000, 10000, 10000));
	public static FPSCounter fpsCounter;
	private static boolean endThread = false;
	public static Game game;

	public static void addCollider(Collider c) {
		synchronized (collidersInGame) {
			if (collidersInGame == null)
				collidersInGame = new ArrayList<Collider>();
			collidersInGame.add(c);
			numOfCollidersInGame++; 
			collisions = new ArrayList<Collision>();
		}
	}

	public GameManager(Game g) {
		fpsCounter = new FPSCounter();
		// FPSCounter.reset();
		// fpsCounter.start();
		game = g;
	}

	
	/** removes the collider with the received colliderID
	 * @param colliderID
	 */
	public static void removeCollider(int colliderID) {
		int i = 0;
		for (Collider collider : collidersInGame) {
			if (collider.getColliderID() == colliderID) {
				collidersInGame.remove(i);
			}
			i++;
		}
	}
	
	@Deprecated
	public static boolean areCollidersColliding(Collider c1, Collider c2){
		for (Collision c : collisions) {
			synchronized (collisions) {
				if((c.c1 == c1 && c.c2 == c2) || (c.c1 == c2 && c.c2 == c1)){
				return true;	
				}	
			}
			
		}
		return false;
	}
	
	@Deprecated
	public static void deleteCollision(Collider c1,Collider c2){
		for (Collision c : collisions) {
			synchronized (collisions) {
				if((c.c1 == c1 && c.c2 == c2) || (c.c1 == c2 && c.c2 == c1)){
				collisions.remove(c);	
				}	
			}
			
		}
	}

	/**
	 * @param c1
	 * @param c2
	 * @return true if colliders c1 and c2 are colliding, else returns false.
	 */
	public static boolean collidersAreColliding(Collider c1, Collider c2) {
		if (collidingColliders == null)
			collidingColliders = new ArrayList<Collider>();
		for (Collider collider : collidingColliders) {
			if (collider.getColliderID() == c1.getColliderID() || collider.getColliderID() == c2.getColliderID())
				return true;
		}
		return false;
	}

	public static void removeColliderFromCollidingList(Collider c) {
		if (collidingColliders == null)
			collidingColliders = new ArrayList<Collider>();
		int i = 0;
		for (Collider collider : collidingColliders) {
			if (collider.getColliderID() == c.getColliderID()) {
				collidingColliders.remove(i);
				break;
			}
			i++;
		}
	}

	/**
	 * @param c1 first {@link Collider}
	 * @param c2 second {@link Collider}
	 * @return the {@link Collider} with a {@link Shape} of type {@link Rectangle} connected {@link GameObject} 
	 */
	public static ArrayList<GameObject> getRectangleGameObjects(Collider c1, Collider c2) {
		ArrayList<GameObject> rectGameObjects = new ArrayList<GameObject>();
		if (c1.getColliderShape() instanceof Rectangle)
			rectGameObjects.add((GameObject) c1.getParent());
		if (c2.getColliderShape() instanceof Rectangle)
			rectGameObjects.add((GameObject) c2.getParent());
		return rectGameObjects;
	}

	/**
	 * @param c1 first {@link Collider}
	 * @param c2 second {@link Collider}
	 * @return the {@link Collider} with a {@link Shape} of type {@link Circle} connected {@link GameObject} 
	 */
	public static ArrayList<GameObject> getCircleGameObjects(Collider c1, Collider c2) {
		ArrayList<GameObject> circleGameObjects = new ArrayList<GameObject>();
		if (c1.getColliderShape() instanceof Circle)
			circleGameObjects.add((GameObject) c1.getParent());
		if (c2.getColliderShape() instanceof Circle)
			circleGameObjects.add((GameObject) c2.getParent());
		return circleGameObjects;
	}

	/** detects and reacts to collision of two {@link Collider}s
	 * @param c1 first {@link Collider}
	 * @param c2 second {@link Collider}
	 */
	public static void detectCollision(Collider c1, Collider c2) {
		// get two Colliders to check if they are colliding
		Collider firstCollider = c1;
		Collider secondCollider = c2;

		// get each collider's corresponding GameObject
		ArrayList<GameObject> circleGameObjects = getCircleGameObjects(c1, c2);
		ArrayList<GameObject> rectGameObjects = getRectangleGameObjects(c1, c2);
		GameObject rectangleGameObject1 = null, rectangleGameObject2 = null, circleGameObject1 = null,
				circleGameObject2 = null;
		if (circleGameObjects.size() == 1 && rectGameObjects.size() == 1) {
			circleGameObject1 = circleGameObjects.get(0);
			rectangleGameObject1 = rectGameObjects.get(0);
			Rectangle r = (Rectangle) rectangleGameObject1.getCollider().getColliderShape();
			Circle c = (Circle) circleGameObject1.getCollider().getColliderShape();
			Vector2d movevec = Vector2d.sub(circleGameObject1.velocity, rectangleGameObject1.velocity);
			//Vector2d movevec = circleGameObject1.velocity;
//			if(!rectangleGameObject1.getName().equals("45Square"))
//				return;
			Vector2d v1 = circleGameObject1.velocity;
			Vector2d v2 = rectangleGameObject1.velocity;
			if (!Collider.areCircleAndRectangleGoingToCollide(r, c, movevec, circleGameObject1, rectangleGameObject1)){
				//System.out.println("leftSide"+r.leftSide);
				//deleteCollision(c1, c2);
				//System.err.println("collision detected!!!!!!!!!!!");
				return;
			}
			//System.out.println("leftSide"+r.leftSide);
			Vector2d hit = Collider.detectCollision(circleGameObject1.collider, rectangleGameObject1.collider);
			Vector2d cornerHit = Collider.detectCornerCollision(circleGameObject1.collider,
					rectangleGameObject1.collider);
			//System.err.println("collided: "+rectangleGameObject1.getName()+"; atPosition:"+circleGameObject1.position+"; hit: "+hit.getX()+","+hit.getY());
			//collisions.add(new Collision(rectangleGameObject1.collider, circleGameObject1.collider, cornerHit));
			if (cornerHit != null) {
				if (!circleGameObject1.getIsDestroyed() && !rectangleGameObject1.getIsDestroyed()) {
					if (!circleGameObject1.isTrigger() && !rectangleGameObject1.isTrigger()) {
						if (GameManager.realisticCollisions) {
							double mass1 = circleGameObject1.mass;
							double mass2 = rectangleGameObject1.mass;
							double newVelX1 = (v1.getX()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * v2.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY1 = (v1.getY()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * v2.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelX2 = (v2.getX()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * v1.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY2 = (v2.getY()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * v1.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double normalizedCircleVelDirection = (new Vector2d(newVelX1, newVelY1)).length();
							Vector2d newVel = Vector2d.mul(normalizedCircleVelDirection, cornerHit.normalized());
							if (circleGameObject1.isMovable)
								circleGameObject1.setVelocity(newVel);
							if (rectangleGameObject1.isMovable)
								rectangleGameObject1.setVelocity(new Vector2d(newVelX2, newVelY2));
							//System.out.println("oops");
							return;
						}
						if (rectangleGameObject1.isMovable)
							rectangleGameObject1.setVelocity(v1);
//						System.out.println("cornerHit"+cornerHit.getX()+","+cornerHit
//								.getY());
						
						if (circleGameObject1.isMovable){
							Vector2d newCornerVelocity = Vector2d.mul(circleGameObject1.getVelocity().length(), cornerHit);
							//System.out.println("CornerVelocity"+newCornerVelocity);
							circleGameObject1.setVelocity(newCornerVelocity);
						}
						circleGameObject1.onCollision(rectangleGameObject1.collider, circleGameObject1);
						rectangleGameObject1.onCollision(circleGameObject1.collider, rectangleGameObject1);
					} if(circleGameObject1.isTrigger() && !rectangleGameObject1.isTrigger()) {
						circleGameObject1.onTrigger(rectangleGameObject1.collider, circleGameObject1);
					}
					if(rectangleGameObject1.isTrigger() && !circleGameObject1.isTrigger()){
						rectangleGameObject1.onTrigger(circleGameObject1.collider, rectangleGameObject1);
					}
					return;
				}
			}

			// if hit is null then nothing is colliding a rectangle
			if (hit != null) {

				// check if the GameObjects are active and thus can be
				// collided with
				if (!circleGameObject1.getIsDestroyed() && !rectangleGameObject1.getIsDestroyed()) {
					if (!circleGameObject1.isTrigger() && !rectangleGameObject1.isTrigger()) {
						if (GameManager.realisticCollisions) {
							double mass1 = circleGameObject1.mass;
							double mass2 = rectangleGameObject1.mass;
							double newVelX1 = (v1.getX()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * v2.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY1 = (v1.getY()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * v2.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelX2 = (v2.getX()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * v1.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY2 = (v2.getY()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * v1.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double normalizedCircleVelDirection = (new Vector2d(newVelX1, newVelY1)).length();
							Vector2d reflectedVel = v1
									.reflect(Vector2d.getNormalOfVector(hit));
							//System.out.println("reflected1" + reflectedVel);
							reflectedVel = Vector2d.mul(normalizedCircleVelDirection, reflectedVel.normalized());
							//System.out.println("reflected2" + reflectedVel);
							//System.out.println("newVel" + new Vector2d(newVelX2, newVelY2));
							if (circleGameObject1.isMovable)
								circleGameObject1.setVelocity(reflectedVel);
							if (rectangleGameObject1.isMovable)
								rectangleGameObject1.setVelocity(new Vector2d(newVelX2, newVelY2));
							return;
						}
						if (rectangleGameObject1.isMovable)
							rectangleGameObject1.setVelocity(v1);
						if (circleGameObject1.isMovable){
							Vector2d newV = v1.reflect(Vector2d.getNormalOfVector(hit.normalized()));
							circleGameObject1.setVelocity(
									newV);
							//System.out.println("hitNormal"+Vector2d.getNormalOfVector(hit.normalized())+"newHitVelocity"+
								//	newV);
						}
						circleGameObject1.onCollision(rectangleGameObject1.collider, circleGameObject1);
						rectangleGameObject1.onCollision(circleGameObject1.collider, rectangleGameObject1);
					} if(circleGameObject1.isTrigger() && !rectangleGameObject1.isTrigger()) {
						circleGameObject1.onTrigger(rectangleGameObject1.collider, circleGameObject1);
					}
					if(rectangleGameObject1.isTrigger() && !circleGameObject1.isTrigger()){
						rectangleGameObject1.onTrigger(circleGameObject1.collider, rectangleGameObject1);
					}
				}
			}
			return;
		} else if (circleGameObjects.size() == 2) {
			circleGameObject1 = circleGameObjects.get(0);
			circleGameObject2 = circleGameObjects.get(1);
			Vector2d v1 = circleGameObject1.velocity;
			Vector2d v2 = circleGameObject2.velocity;
			if (!Collider.areCirclesGoingToCollide(((Circle) circleGameObject1.getCollider().getColliderShape()),
					(Circle) circleGameObject2.getCollider().getColliderShape(),
					Vector2d.sub(circleGameObject1.velocity, circleGameObject2.velocity),circleGameObject1,circleGameObject2)) {
				//deleteCollision(c1, c2);
				return;
			}
			//collisions.add(new Collision(circleGameObject1.collider, circleGameObject2.collider, null));
			if (!circleGameObject1.isTrigger() && !circleGameObject2.isTrigger()) {
				Collider.calculateCollisionOfTwoCircles(circleGameObject1.collider, circleGameObject2.collider,v1,v2);
				circleGameObject1.onCollision(circleGameObject2.collider, circleGameObject1);
				circleGameObject2.onCollision(circleGameObject1.collider, circleGameObject2);
			} if(circleGameObject1.isTrigger() && !circleGameObject2.isTrigger()) {
				circleGameObject1.onTrigger(circleGameObject2.collider, circleGameObject1);
			}
			if(circleGameObject2.isTrigger() && !circleGameObject1.isTrigger()){
				circleGameObject2.onTrigger(circleGameObject1.collider, circleGameObject2);
			}
			return;
		} else if (rectGameObjects.size() == 2) {
			rectangleGameObject1 = rectGameObjects.get(0);
			rectangleGameObject2 = rectGameObjects.get(1);
			// todo rectangle collision
			return;
		}
	}
	
	/**
	 * scans through the {@link GameManager#quad} of type {@link QuadTree} to determine
	 * which {@link Collider}s are likely to collide, and then uses the method {@link GameManager#detectCollision(Collider, Collider)} on them
	 */
	public static void detectCollisions() {
		if (collidersInGame == null || collidersInGame.isEmpty())
			return;
		quad.clear();
		for (int i = 0; i < collidersInGame.size(); i++) {
			quad.insert(collidersInGame.get(i));
		}
		List<Collider> returnObjects = new ArrayList<Collider>();
		for (int i = 0; i < collidersInGame.size(); i++) {
			returnObjects.clear();
			quad.retrieve(returnObjects, collidersInGame.get(i));
			// returnObjects = collidersInGame;
			for (int x = 0; x < returnObjects.size(); x++) {
				// Run collision detection algorithm between objects
				for (int y = 0; y < returnObjects.size(); y++) {
				if(returnObjects.get(x) != returnObjects.get(y)){
				detectCollision(returnObjects.get(y), returnObjects.get(x));
				}
				}
			}
		}
	}

	@Override
	public void run() {
		while (!endThread) {
			// detectCollisions();
			try {
				Thread.sleep((long) Time.deltaTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void terminate() {
		endThread = true;

	}

	/**
	 * Resets all of the {@link GameManager}'s parameters.
	 */
	public static void reset() {
		if (collidersInGame == null)
			collidersInGame = new ArrayList<Collider>();
		collidersInGame.clear();
		numOfCollidersInGame = 0;
		endThread = false;
	}
}
