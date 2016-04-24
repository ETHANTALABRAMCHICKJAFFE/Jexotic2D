package gameMechanics;

import java.awt.Color;
import java.awt.Event;
import java.security.InvalidParameterException;

import gamePieces.GameObject;
import math.Vector2d;

public interface GameBehavior {
	/**
	 * Use this function to create functionality for when collision occurs
	 * @param other
	 */
	public void onCollision(Collider other,GameObject g);
	
	/**
	 * Use this function to create functionality for when trigger occurs.
	 * <br>
	 * A trigger is when an object detects another object has touched him,<br> yet he should not create 
	 * a collision event
	 * @param other
	 */
	public void onTrigger(Collider other,GameObject g);
	
	/**
	 * Use this function to create functionality for when {@link #destroy() destroy} is called
	 */
	public void onDestroy(GameObject g);
	
	/**
	 * Use this function to create functionality for when {@link #restore() restore} is called
	 */
	public void onRestore(GameObject g);
	
	/**
	 * call this to destroy the GameObject, it will make the object invisible and not collidable.
	 */
	public void destroy(GameObject g);
	
	/**
	 * call this to restore a destroyed GameObject.<br>
	 * see: {@link #destroy() destroy}
	 */
	public void restore(GameObject g);
	
	/**
	 * Called every frame when game is running.
	 * @param g
	 */
	public void update(GameObject g);
	
	/**
	 * called when GameObject is created.
	 * @param g
	 */
	public void awake(GameObject g);
	/**
	 * called when adding GameObjects to Game i.e. called when game starts. 
	 * @param g
	 */
	public void start(GameObject g);
	public static GameObject createCopyGameObject(GameObject g){
		GameObject newG = new GameObject(g);
		newG.getCollider().setDrawCollider(false);
		GameManager.game.gameObjects.add(newG);
		return newG;
	}
	public static GameObject createGameObject(String shape,Vector2d position,Vector2d velocity,double width,double height,double mass,Color color,boolean isMovable){
		
		if(shape.equals("Circle")){
		int id = GameManager.game.gameObjects.get(0).collider.getColliderID();
		for (int i = 0; i <GameManager.game.gameObjects.size(); i++) {
			id = Math.max(id,GameManager.game.gameObjects.get(i).collider.getColliderID());
		}
		id++;
		Collider c = new Collider(new Circle(width,360,position),id);
		c.setDrawCollider(true);
		GameObject Circle = new GameObject(position, new Vector2d(velocity), mass, color, c);
		Circle.setName("GameObject"+id);
		GameManager.addCollider(c);
		Circle.setMovable(isMovable);
		Circle.getCollider().setDrawCollider(false);
//		Circle.addScriptFile("DemoScript1.java");
		GameManager.game.gameObjects.add(Circle);
		//GameManager.game.gameObjects.listIterator().add(Circle);
		return Circle;
		}
		else if(shape.equals("Rectangle")){
			int id = GameManager.game.gameObjects.get(0).collider.getColliderID();
			for (int i = 0; i <GameManager.game.gameObjects.size(); i++) {
				synchronized (GameManager.game.gameObjects) {
					id = Math.max(id,GameManager.game.gameObjects.get(i).collider.getColliderID());	
				}
				
			}
			id++;
			Collider c = new Collider(new Rectangle(position,height,width,0),id);
			Vector2d vel = new Vector2d(velocity);
			GameObject rectangle = new GameObject(position, vel , mass, color, c);
			rectangle.setName("GameObject"+id);
			rectangle.setMovable(isMovable);
			rectangle.getCollider().setDrawCollider(false);
			// TODO: remove the script attachment below.
			//rectangle.addScriptFile("DemoScript1.java");
			GameManager.game.gameObjects.add(rectangle);
			//GameManager.game.gameObjects.listIterator().add(rectangle);
			return rectangle;
		}
		else{
			throw new InvalidParameterException("Shape is invalid. Shape is not a 'Circle' or a 'Rectangle'.");
		}
	}
}
