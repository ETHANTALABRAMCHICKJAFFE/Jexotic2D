package gamePieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gameMechanics.Circle;
import gameMechanics.Collider;
import gameMechanics.FPSCounter;
import gameMechanics.GameManager;
import gameMechanics.Input;
import gameMechanics.PhysicsObject;
import gameMechanics.Rectangle;
import gameMechanics.Shape;
import gameMechanics.Time;
import math.Vector2d;

public class Game extends JPanel implements Runnable,KeyListener{
	GameObject padle;
	public ArrayList<GameObject> gameObjects;
	ArrayList<GameObject> collidingGameObjects;
	protected static int numOfFramesRenderedSinceStart = 0;
	protected boolean endThread = false;
	JFrame f;
	
	public ArrayList<GameObject> copyGameObjects(ArrayList<GameObject> gameObjectList){
//		if(this.gameObjectList == null)
//		gameObjectList = new ArrayList<GameObject>();
//		if(!gameObjectList.isEmpty())
//			gameObjectList.clear();
		ArrayList<GameObject> newGameObjects = new ArrayList<GameObject>();
		for (int i = 0; i < gameObjectList.size();i++) {
//			System.out.println("in");
			GameObject currentObject = gameObjectList.get(i);
			//GameObject newG = new GameObject(currentObject.getPosition(),currentObject.getVelocity(),currentObject.getMass(),currentObject.getColor(),new Collider(currentObject.getCollider()));
			GameObject newG = new GameObject(currentObject);
			newG.getCollider().setDrawCollider(false);
			//System.out.println("newG"+newG);
//			GameManager.addCollider(newG.getCollider());
			newG.start(newG);
			newGameObjects.add(newG);
		}
		//System.out.println("newgsdfsd"+newGameObjects);
		return newGameObjects;
	}
	public Game(ArrayList<GameObject> gameObjects) {
		Input input = new Input();
		GameManager gm = new GameManager(this);
		Thread gmt = new Thread(gm,"game manager thread");
		gmt.start();
		this.gameObjects = new ArrayList<GameObject>();
		this.gameObjects.addAll(copyGameObjects(gameObjects));
//		createGameObjects();
		
		f = new JFrame();
		f.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				terminate();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		f.setSize(1600, 900);
		f.add(this);
		//f.addKeyListener(this);
		Thread t = new Thread(this,"Game Thread");
		t.start();
		Time time = new Time();
		Thread timeThread = new Thread(time,"timeThread");
		timeThread.start();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
	
	public void startGameObjects(){
		
	}
	public void terminate() {
		GameManager.terminate();
		Time.terminate();
		FPSCounter.terminate();
		endThread = true;
	}

	public void createWalls() {
		Rectangle topWallShape =  (new Rectangle(new Vector2d(800, 5), 15, 2000, 750));
		Collider topWallCollider = new Collider(topWallShape, 2);
		topWallCollider.setDrawCollider(true);
		GameObject topWall = new GameObject(((Rectangle) topWallShape).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(0, 0), 1, Color.lightGray,topWallCollider);
		
//		topWallCollider.setParent(topWall);
//		GameManager.addCollider(topWallCollider);
		//topWall.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		topWall.getCollider().setDrawCollider(true);
		gameObjects.add(topWall);
		topWall.setMovable(false);
		
		Rectangle leftWallShape =  (new Rectangle(new Vector2d(5, 500), 2000, 15, 750));
		Collider leftWallCollider = new Collider(leftWallShape, 3);
		GameObject leftWall = new GameObject(((Rectangle) leftWallShape).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(0, 0), 1, Color.lightGray,leftWallCollider);
		
//		leftWallCollider.setParent(leftWall);
//		GameManager.addCollider(leftWallCollider);
//		leftWall.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		leftWall.getCollider().setDrawCollider(true);
		gameObjects.add(leftWall);
		leftWall.setMovable(false);

		Rectangle bottomWallShape = (new Rectangle(new Vector2d(800, 840), 15, 2000, 750));
		Collider bottomWallCollider = new Collider(bottomWallShape, 4);
		GameObject bottomWall = new GameObject(((Rectangle) bottomWallShape).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(0, 0), 1, Color.lightGray,bottomWallCollider);
		
//		bottomWallCollider.setParent(bottomWall);
//		GameManager.addCollider(bottomWallCollider);
//		bottomWall.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		bottomWall.getCollider().setDrawCollider(true);
		gameObjects.add(bottomWall);
		bottomWall.setMovable(false);

		Rectangle rightWallShape = (new Rectangle(new Vector2d(1575, 800), 2000, 15, 750));
		Collider rightWallCollider = new Collider(rightWallShape, 5);
		GameObject rightWall = new GameObject(((Rectangle) rightWallShape).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(0, 0), 1, Color.lightGray,rightWallCollider);
		
//		rightWallCollider.setParent(rightWall);
//		GameManager.addCollider(rightWallCollider);
//		rightWall.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		rightWall.getCollider().setDrawCollider(true);
		gameObjects.add(rightWall);
		rightWall.setMovable(false);

	}

	public void createBall() {

		Circle colliderShape = (new Circle(60, 360, new Vector2d(600, 600)));
		Collider gc = new Collider(colliderShape, 0);
		GameObject g = new GameObject(((Circle) colliderShape).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(10, -6), 1, Color.red,gc);
		
//		GameManager.addCollider(gc);
//		gc.setParent(g);
//		g.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		g.getCollider().setDrawCollider(true);
		gameObjects.add(g);
		g.setMovable(true);
//
		for(int i = 0; i<15;i++){
			for(int j = 0; j < 10;j++){
				
		Circle colliderShape2 = (new Circle(20, 360, new Vector2d(400+i*21, 100+j*21)));
		Collider gc2= new Collider(colliderShape2, 1);
		GameObject g2 = new GameObject(((Circle) colliderShape2).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(-10*Math.random(), 6*Math.random()), 1, Color.red,gc2);
		g2.getCollider().setDrawCollider(true);
		gameObjects.add(g2);
		g2.setMovable(true);
		}
		}
	}

	public void createPaddle() {
		Rectangle colliderShape2 =  (new Rectangle(new Vector2d(600, 700), 25, 300, 750));
		Collider gc2 = new Collider(colliderShape2, 1);
		GameObject g2 = new GameObject(((Rectangle) colliderShape2).convertPositionToWorkWithJavaGraphics(),
				new Vector2d(0, 0), 1, Color.blue,gc2);
		
//		GameManager.addCollider(gc2);
//		gc2.setParent(g2);
//		g2.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
		g2.getCollider().setDrawCollider(true);
		gameObjects.add(g2);
		g2.setMovable(false);
		padle = g2;

	}

	public void createBricks() {
		for (int i = 0; i < 10; i++) {
			for(int j = 0; j<4;j++){
			Rectangle colliderShape4 =  (new Rectangle(new Vector2d(250 + i * 110,200+j*60), 50, 100, 500));
			Collider gc4 = new Collider(colliderShape4, 6 + i);
			GameObject g4 = new GameObject(((Rectangle) colliderShape4).convertPositionToWorkWithJavaGraphics(),
					new Vector2d(0,0), 1, Color.ORANGE,gc4);
			
//			gc4.setParent(g4);
//			GameManager.addCollider(gc4);
//			g4.setCollider(GameManager.collidersInGame.get(GameManager.collidersInGame.size() - 1));
			g4.getCollider().setDrawCollider(true);
			gameObjects.add(g4);
			g4.setMovable(false);
			g4.setDestroyOnCollision(true);
			g4.rotate(Math.toRadians(90));
		}
		}
	}

	public void createGameObjects() {

		createWalls();

		createBall();

		createPaddle();

		createBricks();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
//		for (GameObject gameObject : gameObjects) {
		for(int i = 0;i<gameObjects.size();i++){
			GameObject gameObject = gameObjects.get(i);
			gameObject.paintGameObject(g);
			gameObject.getCollider().drawCollider(g);
		}
	}
	

	public void addGameObject(GameObject g){
		gameObjects.add(g);
	}
	@Override
	public void run() {
		GameManager.fpsCounter.start();
		while (!endThread) {
			GameManager.detectCollisions();
			if (gameObjects != null) {
				if (gameObjects.size() > 0) {
					for(int i = 0;i<gameObjects.size();i++){
						GameObject gameObject = gameObjects.get(i);
//					for (GameObject gameObject : gameObjects) {
						gameObject.update(gameObject);
						repaint();
						
						numOfFramesRenderedSinceStart++;
					}
					
				}
			}
			GameManager.fpsCounter.interrupt();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			try {
//				Thread.sleep(15);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
		}
	}

//	public static void main(String[] args) {
//		Game g = new Game(null);
//	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		/*
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			padle.getCollider().updatePosition(new Vector2d(40, 0));
			//padle.setVelocity(new Vector2d(40,0));
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			padle.getCollider().updatePosition(new Vector2d(-40, 0));
		//padle.updateVelocity(padle);
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			System.out.println("up");
			if (Time.timeScale == 1)
				Time.timeScale = 0;
			else
				Time.timeScale = 1;
			System.out.println(Time.timeScale);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			System.out.println("up");
			if (Time.timeScale == 1)
				Time.timeScale = -1;
			else
				Time.timeScale = 1;
			System.out.println(Time.timeScale);
		}
		*/
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
