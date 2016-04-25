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

	public static void removeCollider(int colliderID) {
		int i = 0;
		for (Collider collider : collidersInGame) {
			if (collider.getColliderID() == colliderID) {
				collidersInGame.remove(i);
			}
			i++;
		}
	}

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
	public static void deleteCollision(Collider c1,Collider c2){
		for (Collision c : collisions) {
			synchronized (collisions) {
				if((c.c1 == c1 && c.c2 == c2) || (c.c1 == c2 && c.c2 == c1)){
				collisions.remove(c);	
				}	
			}
			
		}
	}
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

	public static ArrayList<GameObject> getRectangleGameObjects(Collider c1, Collider c2) {
		ArrayList<GameObject> rectGameObjects = new ArrayList<GameObject>();
		if (c1.getColliderShape() instanceof Rectangle)
			rectGameObjects.add((GameObject) c1.getParent());
		if (c2.getColliderShape() instanceof Rectangle)
			rectGameObjects.add((GameObject) c2.getParent());
		return rectGameObjects;
	}

	public static ArrayList<GameObject> getCircleGameObjects(Collider c1, Collider c2) {
		ArrayList<GameObject> circleGameObjects = new ArrayList<GameObject>();
		if (c1.getColliderShape() instanceof Circle)
			circleGameObjects.add((GameObject) c1.getParent());
		if (c2.getColliderShape() instanceof Circle)
			circleGameObjects.add((GameObject) c2.getParent());
		return circleGameObjects;
	}

	public void reactToCollisions() {
		for (int i = 0; i < collisions.size(); i++) {
			Collision collision = collisions.get(i);
			Collider c1 = collision.c1;
			Collider c2 = collision.c2;
			Shape s1 = c1.getColliderShape();
			Shape s2 = c2.getColliderShape();
			if (collision instanceof TwoCircleCollision) {

				Collider.calculateCollisionOfTwoCircles(c1, c2);
				GameObject g1 = ((GameObject) c1.getParent());
				GameObject g2 = ((GameObject) c2.getParent());
				if (g1.isTrigger()) {
					g1.onTrigger(c2, g1);
				} else {
					g1.onCollision(c2, g1);
				}
				if (g2.isTrigger()) {
					g2.onTrigger(c1, g2);
				} else {
					g2.onCollision(c1, g2);
				}

				collisions.remove(i);
				continue;
			}
			if (collision instanceof CircleRectCollision) {
				CircleRectCollision crCollision = (CircleRectCollision) collision;
				if (crCollision.isCornerCollision) {
					Collider circle = crCollision.getCircleCollider();
					circle.getParent().setVelocity(
							Vector2d.mul(circle.getParent().getVelocity().length(), crCollision.hit.normalized()));
					GameObject g1 = ((GameObject) c1.getParent());
					GameObject g2 = ((GameObject) c2.getParent());
					g1.onCollision(c2, g1);
					g2.onCollision(c1, g2);
					g1.onTrigger(c2, g1);
					g2.onTrigger(c1, g2);
					collisions.remove(i);
					continue;
				}
				if (!crCollision.isCornerCollision) {
					Collider circle = crCollision.getCircleCollider();
					circle.getParent().setVelocity(
							circle.getParent().getVelocity().reflect(Vector2d.getNormalOfVector(crCollision.hit)));
					GameObject g1 = ((GameObject) c1.getParent());
					GameObject g2 = ((GameObject) c2.getParent());
					g1.onCollision(c2, g1);
					g2.onCollision(c1, g2);
					g1.onTrigger(c2, g1);
					g2.onTrigger(c1, g2);
					collisions.remove(i);
					continue;
				}
			}

		}
		collisions.clear();
	}

	public static void detectCollision(Collider c1, Collider c2) {
		// get two Colliders to check if they are colliding
		Collider firstCollider = c1;
		Collider secondCollider = c2;

		// get each collider's corresponding GameObject
		// GameObject firstGameObject = (GameObject) firstCollider.getParent();
		// GameObject secondGameObject = (GameObject)
		// secondCollider.getParent();
		// Vector2d firstGameObjectVelocity = firstGameObject.getVelocity();
		// Vector2d secondGameObjectVelocity = secondGameObject.getVelocity();
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
			if (!Collider.areCircleAndRectangleGoingToCollide(r, c, movevec)){
				//deleteCollision(c1, c2);
				return;
			}

			Vector2d hit = Collider.detectCollision(circleGameObject1.collider, rectangleGameObject1.collider);
			Vector2d cornerHit = Collider.detectCornerCollision(circleGameObject1.collider,
					rectangleGameObject1.collider);
			//collisions.add(new Collision(rectangleGameObject1.collider, circleGameObject1.collider, cornerHit));
			if (cornerHit != null) {
				if (!circleGameObject1.getIsDestroyed() && !rectangleGameObject1.getIsDestroyed()) {
					if (!circleGameObject1.isTrigger() && !rectangleGameObject1.isTrigger()) {
						if (GameManager.realisticCollisions) {
							double mass1 = circleGameObject1.mass;
							double mass2 = rectangleGameObject1.mass;
							double newVelX1 = (circleGameObject1.velocity.getX()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * rectangleGameObject1.velocity.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY1 = (circleGameObject1.velocity.getY()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * rectangleGameObject1.velocity.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelX2 = (rectangleGameObject1.velocity.getX()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * circleGameObject1.velocity.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY2 = (rectangleGameObject1.velocity.getY()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * circleGameObject1.velocity.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							// circleGameObject1.setVelocity(new
							// Vector2d(newVelX1,newVelY1));
							double normalizedCircleVelDirection = (new Vector2d(newVelX1, newVelY1)).length();
							// normalizedCircleVelDirection =
							// normalizedCircleVelDirection.normalized();
							Vector2d newVel = Vector2d.mul(normalizedCircleVelDirection, cornerHit.normalized());
							// newVel =
							// Vector2d.mul(normalizedCircleVelDirection,newVel.normalized());
							if (circleGameObject1.isMovable)
								circleGameObject1.setVelocity(newVel);
							if (rectangleGameObject1.isMovable)
								rectangleGameObject1.setVelocity(new Vector2d(newVelX2, newVelY2));
							System.out.println("oops");
							return;
						}
						if (rectangleGameObject1.isMovable)
							rectangleGameObject1.setVelocity(circleGameObject1.getVelocity());
						if (circleGameObject1.isMovable)
							circleGameObject1.setVelocity(
									Vector2d.mul(circleGameObject1.getVelocity().length(), cornerHit.normalized()));

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
							double newVelX1 = (circleGameObject1.velocity.getX()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * rectangleGameObject1.velocity.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY1 = (circleGameObject1.velocity.getY()
									* (circleGameObject1.mass - rectangleGameObject1.mass)
									+ (2 * rectangleGameObject1.mass * rectangleGameObject1.velocity.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelX2 = (rectangleGameObject1.velocity.getX()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * circleGameObject1.velocity.getX()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							double newVelY2 = (rectangleGameObject1.velocity.getY()
									* (rectangleGameObject1.mass - circleGameObject1.mass)
									+ (2 * circleGameObject1.mass * circleGameObject1.velocity.getY()))
									/ (circleGameObject1.mass + rectangleGameObject1.mass);
							// circleGameObject1.setVelocity(new
							// Vector2d(newVelX1,newVelY1));
							double normalizedCircleVelDirection = (new Vector2d(newVelX1, newVelY1)).length();
							// normalizedCircleVelDirection =
							// normalizedCircleVelDirection.normalized();
							Vector2d reflectedVel = circleGameObject1.getVelocity()
									.reflect(Vector2d.getNormalOfVector(hit));
							System.out.println("reflected1" + reflectedVel);
							reflectedVel = Vector2d.mul(normalizedCircleVelDirection, reflectedVel.normalized());
							System.out.println("reflected2" + reflectedVel);
							System.out.println("newVel" + new Vector2d(newVelX2, newVelY2));
							if (circleGameObject1.isMovable)
								circleGameObject1.setVelocity(reflectedVel);
							if (rectangleGameObject1.isMovable)
								rectangleGameObject1.setVelocity(new Vector2d(newVelX2, newVelY2));
							return;
						}
						if (rectangleGameObject1.isMovable)
							rectangleGameObject1.setVelocity(circleGameObject1.getVelocity());
						if (circleGameObject1.isMovable)
							circleGameObject1.setVelocity(
									circleGameObject1.getVelocity().reflect(Vector2d.getNormalOfVector(hit)));
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
			// System.out.println(circleGameObject1);
			if (!Collider.areCirclesGoingToCollide(((Circle) circleGameObject1.getCollider().getColliderShape()),
					(Circle) circleGameObject2.getCollider().getColliderShape(),
					Vector2d.sub(circleGameObject1.velocity, circleGameObject2.velocity))) {
				//deleteCollision(c1, c2);
				return;
			}
			// System.out.println("<first>"+circleGameObject1+"</first><second>"+circleGameObject2+"</second>");
			//collisions.add(new Collision(circleGameObject1.collider, circleGameObject2.collider, null));
			if (!circleGameObject1.isTrigger() && !circleGameObject2.isTrigger()) {
				Collider.calculateCollisionOfTwoCircles(circleGameObject1.collider, circleGameObject2.collider);
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
			//int colls = 0;
			for (int x = 0; x < returnObjects.size(); x++) {
				// Run collision detection algorithm between objects
				for (int y = 0; y < returnObjects.size(); y++) {
				if(returnObjects.get(x) != returnObjects.get(y)){
				detectCollision(returnObjects.get(y), returnObjects.get(x));
				//System.out.println("colls"+(++colls));
				}
				}
				//}
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

	public static void reset() {
		if (collidersInGame == null)
			collidersInGame = new ArrayList<Collider>();
		collidersInGame.clear();
		numOfCollidersInGame = 0;
		endThread = false;
	}
}
