package gamePieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.security.cert.PKIXRevocationChecker.Option;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.RepaintManager;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import editor.ProjectManager;
import gameMechanics.Circle;
import gameMechanics.Collider;
import gameMechanics.GameBehavior;
import gameMechanics.GameManager;
import gameMechanics.IDestroyable;
import gameMechanics.PhysicsObject;
import gameMechanics.Rectangle;
import gameMechanics.Shape;
import gameMechanics.Square;
import gameMechanics.Time;
import math.Vector2d;

/**
 * @author Ethan
 * GameObject, anything that runs in the Game is a GameObject. It represents an Object
 * that the engine know how to run Physics and calculations on. It combines all
 * the features into one Object that it is used to execute gameplay.
 */
public class GameObject extends PhysicsObject implements GameBehavior,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color;
	private boolean isDestroyed = false;
	private boolean destroyOnCollision = false;
	private boolean isTrigger = false;
	private String tag = "Default",name = "gameObject";
	transient private ArrayList<GameBehavior> scripts;
	private ArrayList<String> scriptsPaths;

	public GameObject(Vector2d position, Vector2d velocity, double mass, Color c,Collider collider) {
		super(position, velocity, mass);
		color = c;
		setCollider(collider);
		scripts = new ArrayList<GameBehavior>();
		scriptsPaths = new ArrayList<String>();
		awake(this);
	}
	
	public GameObject(GameObject g){
		super(g.position,g.velocity,g.mass);
		color = g.color;
		isMovable = g.isMovable;
		isDestroyed = g.isDestroyed;
		name = g.name;
		tag = g.tag;
		Collider newCollider = new Collider(g.collider);
		setCollider(newCollider);
		GameManager.addCollider(newCollider);
		setTrigger(g.isTrigger());
		scripts = new ArrayList<GameBehavior>();
		scriptsPaths = new ArrayList<String>();
		if(g.scripts != null && !g.scripts.isEmpty())
			scripts.addAll(g.scripts);
		if(g.scriptsPaths != null && !g.scriptsPaths.isEmpty())
			scriptsPaths.addAll(g.scriptsPaths);
		if(!scriptsPaths.isEmpty()){
			
			synchronized (scriptsPaths) {
				for (String path : scriptsPaths) {
					addScriptFromFile(path);	
				}
			}
			
			
		}
		awake(this);
	}

	
	/** adds a script to the scripts ArrayList
	 * @param script the instance to add.
	 * @param scriptPath the location path of the script.
	 */
	public void addScript(GameBehavior script,String scriptPath){
		scripts.add(script);
		scriptsPaths.add(scriptPath);
	}
	
	@Deprecated
	/** adds a script from a file in the file system.
	 * @param scriptName
	 */
	public void addScriptFromFile(String scriptName){
		try{
			// Save source in .java file.
			File root = new File(ProjectManager.projectDirectory); // On Windows running on C:\, this is C:\java.
			File sourceFile = new File(root,"\\Scripts\\"+scriptName+".java");
			System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_60");
			
			// Compile source file.
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			System.out.println(sourceFile.getPath());
			compiler.run(System.in, System.out, System.err, sourceFile.getPath());

			URL[] classes = {root.toURI().toURL()};
		    URLClassLoader child = new URLClassLoader (classes, this.getClass().getClassLoader());
			Class<?> cls = Class.forName("Scripts."+scriptName, false, child); 
			Object instance = cls.newInstance(); 
			System.out.println(instance);
			scripts.add((GameBehavior)instance);
			}catch(IOException e){
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
				System.out.println(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				System.out.println(e);
			}
		
	}

	/**
	 * adds a script from a file in the file system.
	 * @param scriptname the name of the script
	 */
	public void addScriptFile(String scriptname){
		try{
			// Save source in .java file.
			File root = new File(ProjectManager.projectDirectory);
			File sourceFile = new File(root, "\\Scripts\\"+scriptname+".java");
			sourceFile.getParentFile().mkdirs();
			
			System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_60");
			
			// Compile source file.
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			System.out.println(sourceFile.getPath());
			compiler.run(System.in, System.out, System.err, sourceFile.getPath());

			URL[] classes = {root.toURI().toURL()};
		    URLClassLoader child = new URLClassLoader (classes, this.getClass().getClassLoader());
			Class<?> cls = Class.forName("Scripts."+scriptname, false, child);
			Object instance = cls.newInstance(); 
			System.out.println(instance);
			addScript((GameBehavior)instance,scriptname);
			}catch(IOException e){
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
				System.out.println(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				System.out.println(e);
			}
	}
	
	
	/**
	 * draws the GameObject using the g Graphics component
	 * 
	 * @param g
	 */
	public void paintGameObject(Graphics g) {
		if(isDestroyed)
			return;
		Graphics2D g2 = (Graphics2D)g.create();
		 /* Enable anti-aliasing and pure stroke */
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (getCollider().getColliderShape() instanceof Circle) {
			Circle c = (Circle) getCollider().getColliderShape();
			g2.setColor(color);
			Vector2d currentPosition = position;
			double angle = collider.getColliderShape().getRotationAngle();
			angle = Math.toRadians(angle);
			g2.rotate(angle, position.getX(), position.getY());
			
			g2.fill(new Ellipse2D.Double(currentPosition.getX()-c.getRadius(),currentPosition.getY()-c.getRadius(),c.getRadius()*2,c.getRadius()*2));

		} else if (getCollider().getColliderShape() instanceof Rectangle) {
			Rectangle r = (Rectangle) getCollider().getColliderShape();
			g2.setColor(color);
			Vector2d currentPosition = position;
			java.awt.Rectangle.Double rect = new java.awt.Rectangle.Double(currentPosition.getX()-r.getLengthOfSideB()/2,currentPosition.getY()-r.getLengthOfSideA()/2,r.getLengthOfSideB(),r.getLengthOfSideA());
			double angle = collider.getColliderShape().getRotationAngle();
			angle = Math.toRadians(angle);
			g2.rotate(angle, position.getX(), position.getY());
			g2.fill(rect);
			//drawClosedShape(r,g2);
			// draw your rectangle here...
			
			
		}
		g2.dispose();
	}

	/** draws the collider
	 * @param collider the shape of the collider to draw
	 * @param g2 the Graphics2D
	 */
	public void drawClosedShape(Shape collider,Graphics2D g2){
		ArrayList<Vector2d> newPoints = null;
		newPoints = collider.getPoints();
		if(newPoints == null || newPoints.isEmpty())
			return;
		for (int i = 0; i < newPoints.size() - 1; i++) {
			double x1 = newPoints.get(i).getX();
			double y1 = newPoints.get(i).getY();
			double x2 = newPoints.get(i + 1).getX();
			double y2 = newPoints.get(i + 1).getY();
			
			g2.draw(new Line2D.Double(x1, y1, x2, y2));	
		}
		
		double x3 = collider.getPoints().get(0).getX();
		double y3 = collider.getPoints().get(0).getY();
		double x4 = collider.getPoints().get(collider.getPoints().size() - 1).getX();
		double y4 = collider.getPoints().get(collider.getPoints().size() - 1).getY();
		g2.draw(new Line2D.Double(x3, y3, x4, y4));
	}
	
	/** rotate the GameObject
	 * @param angle the angle in degrees to rotate
	 */
	public void rotate(double angle){
		
		Shape s = collider.getColliderShape();
		if(angle == s.getRotationAngle())
			return;
		if(s instanceof Rectangle)
		((Rectangle)collider.getColliderShape()).rotate(angle);
		else if (s instanceof Circle)
			((Circle)collider.getColliderShape()).rotate(angle);
		else
			collider.getColliderShape().rotate(angle);
	}
	
	
	@Override
	public void update(GameObject g) {
		if(Time.timeScale <= 0)
			return;
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.update(g);
		}
		getCollider().updatePosition(Vector2d.mul(Time.timeScale,Vector2d.mul(Time.deltaTime,velocity)));
		//System.out.println("rotatiionAngle"+collider.getColliderID()+":"+collider.getColliderShape().getRotationAngle());
		//rotate(collider.getColliderShape().getRotationAngle()+1);
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void destroy(GameObject g) {
		if(!isDestroyed){
			for (GameBehavior gameBehavior : scripts) {
				gameBehavior.destroy(g);
			}
			isDestroyed = true;
			onDestroy(g);
		}
		
	}

	@Override
	public void restore(GameObject g) {
		if(!isDestroyed){
			for (GameBehavior gameBehavior : scripts) {
				gameBehavior.restore(g);
			}
			isDestroyed = !isDestroyed;
			onRestore(g);
		}
		
		//collider.setCollidable(true);
	}

	/**
	 * @return the destroyOnCollision
	 */
	public boolean isDestroyOnCollision() {
		return destroyOnCollision;
	}

	/**
	 * @param destroyOnCollision the destroyOnCollision to set
	 */
	public void setDestroyOnCollision(boolean destroyOnCollision) {
		this.destroyOnCollision = destroyOnCollision;
	}

	public void setIsDestroyed(boolean isDestroyed){
		this.isDestroyed = isDestroyed;
	}

	public boolean getIsDestroyed(){
		return isDestroyed;
	}

	@Override
	public void onCollision(Collider other,GameObject g) {
		if(destroyOnCollision){
			destroy(g);
		}
		
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onCollision(other,g);
		}
	}

	@Override
	public void onTrigger(Collider other,GameObject g) {
		if(!isTrigger()){
			return;
		}
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onTrigger(other,g);
		}
	}

	@Override
	public void onDestroy(GameObject g) {
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onDestroy(g);
		}
	}

	@Override
	public void onRestore(GameObject g) {
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onRestore(g);
		}
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isTrigger
	 */
	public boolean isTrigger() {
		return isTrigger;
	}

	/**
	 * @param isTrigger the isTrigger to set
	 */
	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}

	@Override
	public void awake(GameObject g) {
		// TODO Auto-generated method stub
		for(GameBehavior b : scripts){
			b.awake(g);
		}
	}

	@Override
	public void start(GameObject g) {
		// TODO Auto-generated method stub
		for(GameBehavior b : scripts){
			b.start(g);
		}
	}
}
