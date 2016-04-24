package gamePieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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

public class GameObject extends PhysicsObject implements GameBehavior,Serializable{

	private Color color;
	private boolean isDestroyed = false;
	private boolean destroyOnCollision = false;
	private boolean isTrigger = false;
	private String tag = "Default",name = "gameObject";
	private ArrayList<GameBehavior> scripts;
	public GameObject(Vector2d position, Vector2d velocity, double mass, Color c,Collider collider) {
		super(position, velocity, mass);
		color = c;
		setCollider(collider);
		//GameManager.addCollider(collider);
		scripts = new ArrayList<GameBehavior>();
		awake(this);
	}
	
	public GameObject(GameObject g){
		super(g.position,g.velocity,g.mass);
		color = g.color;
		Collider newCollider = new Collider(g.collider);
		//newCollider.setParent(this);
		setCollider(newCollider);
		//GameManager.collidersInGame.clear();
		GameManager.addCollider(newCollider);
		isMovable = g.isMovable;
		isDestroyed = g.isDestroyed;
		setTrigger(g.isTrigger());
		name = g.name;
		tag = g.tag;
		scripts = new ArrayList<GameBehavior>();
		scripts.addAll(g.scripts);
	}

	public void addScript(GameBehavior script){
		scripts.add(script);
	}
	
	public void addScriptFile(String filename){
		try{
//		 Prepare source somehow.
		String source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";
		//source = fileSource;
		// Save source in .java file.
		File root = new File("C:\\java"); // On Windows running on C:\, this is C:\java.
		File sourceFile = new File(root, "test/DemoScript1.java");
//		File sourceFile = new File(root, "C:\\Users\\Ethan\\workspace\\AtariBreakoutFor2\\Scripts\\"+filename);
		sourceFile.getParentFile().mkdirs();
		//CharSequence c = source;
//		//Files.write(path, bytes, options)
//		Files.write(sourceFile.toPath(),source.getBytes(),(OpenOption)StandardOpenOption.WRITE);
		
		System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_60");
		
		// Compile source file.
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		sourceFile = new File("C:\\java\\test\\DemoScript1.java");
//		sourceFile = new File("C:\\Users\\Ethan\\workspace\\AtariBreakoutFor2\\Scripts\\myScripts\\"+filename);
		System.out.println(sourceFile.getPath());
		compiler.run(System.in, System.out, System.err, sourceFile.getPath());

		// Load and instantiate compiled class.
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
		Class<?> cls = Class.forName("test.DemoScript1", false, classLoader); // Should print "hello".
		Object instance = cls.newInstance(); // Should print "world".
		System.out.println(instance); // Should print "test.Test@hashcode".
		addScript((GameBehavior)instance);
		}catch(IOException e){
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
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
		Graphics2D g2 = (Graphics2D)g;
//		if (getCollider().getColliderShape() instanceof Circle) {
//			Circle c = (Circle) getCollider().getColliderShape();
//			g.setColor(color);
//			g.fillOval((int) getPosition().getX(), (int) getPosition().getY(), (int) c.getRadius(),
//					(int) c.getRadius());
//
//		} else if (getCollider().getColliderShape() instanceof Rectangle) {
//			Rectangle r = (Rectangle) getCollider().getColliderShape();
//			g.setColor(color);
//			Vector2d convertedPos = getPosition();
//			g.fillRect((int) convertedPos.getX(), (int) convertedPos.getY(), (int) r.getLengthOfSideB(),
//					(int) r.getLengthOfSideA());
//		}
		 /* Enable anti-aliasing and pure stroke */
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (getCollider().getColliderShape() instanceof Circle) {
			Circle c = (Circle) getCollider().getColliderShape();
			g2.setColor(color);
			//g2.rotate(collider.getColliderShape().getRotationAngle());
			//g2.translate(c.getReferencePoint().getX()+c.getRadius(), c.getReferencePoint().getY()+c.getRadius());
			Vector2d currentPosition = position;
			g2.fill(new Ellipse2D.Double(currentPosition.getX()-c.getRadius(),currentPosition.getY()-c.getRadius(),c.getRadius()*2,c.getRadius()*2));
//			g2.fillOval((int) getPosition().getX(), (int) getPosition().getY(), (int) c.getRadius(),
//					(int) c.getRadius());

		} else if (getCollider().getColliderShape() instanceof Rectangle) {
			Rectangle r = (Rectangle) getCollider().getColliderShape();
			g2.setColor(color);
			//java.awt.Rectangle rect = new java.awt.Rectangle((int)(r.getReferencePoint().getX()-r.getLengthOfSideB()/2),(int)(r.getReferencePoint().getY()-r.getLengthOfSideA()/2),(int)r.getLengthOfSideB(),(int)r.getLengthOfSideA());
			Vector2d currentPosition = position;
			java.awt.Rectangle.Double rect = new java.awt.Rectangle.Double(currentPosition.getX()-r.getLengthOfSideB()/2,currentPosition.getY()-r.getLengthOfSideA()/2,r.getLengthOfSideB(),r.getLengthOfSideA());
			//Vector2d convertedPos = getPosition();
			//g2.translate(rect.getX(), r.getReferencePoint().getY());
			//AffineTransform transform = new AffineTransform();
			//transform.rotate(90, rect.getX(), rect.getY());
			//AffineTransform old = g2.getTransform();
			//g2.transform(transform);
			// draw your rectangle here...	
			//g2.setTransform(old);
			//g2.translate(r.getReferencePoint().getX(), r.getReferencePoint().getY());
			//g2.rotate(r.getRotationAngle(),r.getReferencePoint().getX()+r.getLengthOfSideB()/2, r.getReferencePoint().getY()+r.getLengthOfSideA()/2);
//			AffineTransform transform = new AffineTransform();
			//transform.rotate(r.getRotationAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
//			transform.rotate(r.getRotationAngle());
//			AffineTransform old = g2.getTransform();
//			g2.transform(transform);
			//System.out.println("rotate"+r.getRotationAngle());
			//g2.rotate(r.getRotationAngle());
			// draw your rectangle here...
			g2.fill(rect);
//			g2.setTransform(old);//g2.fill(rect);
			//g2.rotate(0);
			//g2.translate(-r.getReferencePoint().getX(), -r.getReferencePoint().getY());
			
			//g2.translate(-rect.getWidth() / 2, -rect.getHeight() / 2);
			
//			g2.fillRect((int) convertedPos.getX(), (int) convertedPos.getY(), (int) r.getLengthOfSideB(),
//					(int) r.getLengthOfSideA());
			
		}
	}

	public void rotate(double angle){
		Shape s = collider.getColliderShape();
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
//	public void OnCollision(){
//		if(destroyOnCollision){
//			destroy();
//		}
//	}
//	
	public void setIsDestroyed(boolean isDestroyed){
		this.isDestroyed = isDestroyed;
	}
	public boolean getIsDestroyed(){
		return isDestroyed;
	}

	@Override
	public void onCollision(Collider other,GameObject g) {
		// TODO Auto-generated method stub
		if(destroyOnCollision){
			destroy(g);
		}
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onCollision(other,g);
		}
	}

	@Override
	public void onTrigger(Collider other,GameObject g) {
		// TODO Auto-generated method stub
		if(!isTrigger()){
			return;
		}
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onTrigger(other,g);
		}
	}

	@Override
	public void onDestroy(GameObject g) {
		// TODO Auto-generated method stub
		for (GameBehavior gameBehavior : scripts) {
			gameBehavior.onDestroy(g);
		}
	}

	@Override
	public void onRestore(GameObject g) {
		// TODO Auto-generated method stub
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
