package editor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import gameMechanics.Circle;
import gameMechanics.Collider;
import gameMechanics.GameBehavior;
import gameMechanics.GameManager;
import gameMechanics.Rectangle;
import gameMechanics.Time;
import gamePieces.*;
import math.Vector2d;

public class MainEditor extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	ArrayList<GameObject> gameObjects;
	ArrayList<Shape> gameItems;

	int numOfGameObjects = 0;

	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;
	Inspector inspector;
	Outliner outliner;

	public MainEditor() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
		gameObjects = new ArrayList<GameObject>();
		gameItems = new ArrayList<>();
		setBackground(Color.white);
		// createMenu();
		// f.add(inspector);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		Time.timeScale = 0;

		// createGame();
	}

	public void addJMenu(JFrame f) {
		// Create the menu bar.
		menuBar = new JMenuBar();

		// file menu
		menu = new JMenu("File");
		menuItem = new JMenuItem("Save");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("save");
		menu.add(menuItem);
		menuBar.add(menu);
		
		// Build the first menu.
		menu = new JMenu("Create");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menu.add(menuItem);

		menuItem = new JMenuItem("Both text and icon");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		// a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		// a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		// a submenu
		menu.addSeparator();
		submenu = new JMenu("Shapes");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("Circle");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		menuItem.setActionCommand("circle");
		menuItem.getText();
		submenu.add(menuItem);

		menuItem = new JMenuItem("Rectangle");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		menuItem.setActionCommand("rect");
		submenu.add(menuItem);

		menu.add(submenu);

		// Build second menu in the menu bar.
		menu = new JMenu("Game Settings");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");

		// a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("Realistic collisions");
		// cbMenuItem.setMnemonic(KeyEvent.VK_C)
		cbMenuItem.addActionListener(this);
		cbMenuItem.setActionCommand("realisticColl");
		menu.add(cbMenuItem);

		menuBar.add(menu);

		f.setJMenuBar(menuBar);

	}

	public void createPanels(JFrame f) {

		addJMenu(f);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		f.add(this, BorderLayout.CENTER);

		addInspector(f);
		addOutliner(f);
		// f.setVisible(true);

	}

	public void addOutliner(JFrame f) {

		outliner = new Outliner(this);
		int width = 300, height = f.getHeight();
		outliner.setPreferredSize(new Dimension(width, height));
		// outliner.setSize(new Dimension(outliner.getWidth(),height));
		outliner.setBackground(Color.lightGray);
		outliner.setVisible(true);
		f.add(outliner, BorderLayout.WEST);
	}

	public void addInspector(JFrame f) {

		// f.addKeyListener(this);

		inspector = new Inspector(this);
		int width = 300, height = f.getHeight();
		;
		inspector.setPreferredSize(new Dimension(width, height));
		// inspector.setSize(new Dimension(inspector.getWidth(),height));
		inspector.setBackground(Color.DARK_GRAY);
		inspector.setVisible(true);
		f.add(inspector, BorderLayout.EAST);
	}

	Game g;

	public void createGame() {

		// System.out.println("gameObjects");
		GameManager.reset();
		Time.reset();
		g = new Game(gameObjects);
		Time.timeScale = 1;
		// g.createGameObjects();
	}

	public void createGUI() {
		MainEditor m = this;
		JFrame frame = new JFrame("Jexotic2D");
		Image icon = Toolkit.getDefaultToolkit().getImage("Images/JexoticIcon1.png");
	    frame.setIconImage(icon);
		frame.add(m);
		m.createPanels(frame);
		JPanel topPanel = new JPanel();
		JButton playButton = new JButton("Play");
		playButton.setActionCommand("play");
		playButton.addActionListener(m);
		topPanel.add(playButton);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.setVisible(true);
	}

	/*
	 * public static void main(String[] args) { MainEditor m = new MainEditor();
	 * JFrame frame = new JFrame("Jexotic2D"); frame.add(m);
	 * m.createPanels(frame); JPanel topPanel = new JPanel(); JButton playButton
	 * = new JButton("Play"); playButton.setActionCommand("play");
	 * playButton.addActionListener(m); topPanel.add(playButton);
	 * frame.add(topPanel,BorderLayout.NORTH); frame.setVisible(true); }
	 */

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).paintGameObject(g);
			gameObjects.get(i).getCollider().drawCollider(g);
			if (gameObjects.get(i) == inspector.gameObject) {
				g2.setColor(Color.CYAN);
				g2.setStroke(new BasicStroke(1.5f));
				gameMechanics.Shape s = gameObjects.get(i).getCollider().getColliderShape();
				if (s instanceof Circle) {
					double width = s.getShapeWidth(), height = s.getShapeHeight();
					double newWidth = width * 2, newHeight = height * 2;
					g2.draw(new Rectangle2D.Double(s.getReferencePoint().getX() - (newWidth - width),
							s.getReferencePoint().getY() - (newHeight - height), newWidth, newHeight));
				} else if (s instanceof Rectangle) {
					double width = s.getShapeWidth(), height = s.getShapeHeight();
					double newWidth = width * 2, newHeight = height * 2;
					g2.draw(new Rectangle2D.Double(s.getReferencePoint().getX() - (newWidth - width),
							s.getReferencePoint().getY() - (newHeight - height), newWidth, newHeight));

				}
			}
		}
	}

	public void addCircle() {
		Vector2d position = new Vector2d(Math.random() * 500 + 200, Math.random() * 500 + 50);
		double radius = 15;
		Collider c = new Collider(new Circle(radius, 360, position), numOfGameObjects);
		c.setDrawCollider(true);
		GameObject Circle = new GameObject(position, new Vector2d(-3 * Math.random() + 5, Math.random() * 10),
				Math.random() * 10 + 1, Color.red, c);
		Circle.setName("GameObject" + numOfGameObjects);
		// Circle.addScriptFile("DemoScript1.java");
		gameObjects.add(Circle);
		numOfGameObjects++;
		inspector.setGameObject(Circle);
		outliner.addGameObject(Circle);
		outliner.createButtons();
		outliner.updateSelected(Circle);
		grabFocus();
		// GameManager.addCollider(c);
		// private Shape rect = new Rectangle2D.Double(position.getX(),
		// position.getY(), =, 100);
		// Shape circleShape = new Ellipse2D.Double(Circle.getPosition().getX(),
		// Circle.getPosition().getY(), radius,radius);
		// gameItems.add(circleShape);
		// System.out.println(gameObjects);
	}

	public void addRectangle() {
		Vector2d position = new Vector2d(Math.random() * 500 + 200, Math.random() * 500 + 50);
		// double radius = 20;
		double height = 50, width = 50;
		Rectangle r = new Rectangle(position, height, width, 0);
		r.rotate(45);
		//r.setRotationAngle(45);
		Collider c = new Collider(r, numOfGameObjects);
		c.setDrawCollider(true);
		Vector2d vel = new Vector2d(-3 * Math.random() + 5, Math.random() * 10);
		vel = Vector2d.zero();
		GameObject rectangle = new GameObject(position, vel, 1, Color.red, c);
		rectangle.setName("GameObject" + numOfGameObjects);
		rectangle.setMovable(false);
		rectangle.addScriptFile("DemoScript1");
		gameObjects.add(rectangle);
		numOfGameObjects++;
		inspector.setGameObject(rectangle);
		outliner.addGameObject(rectangle);
		outliner.createButtons();
		outliner.updateSelected(rectangle);
		grabFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "save"){
			ProjectManager.saveProject(this);
		}
		if (e.getActionCommand() == "circle") {
			addCircle();
			repaint();
		}
		if (e.getActionCommand() == "rect") {
			addRectangle();
			repaint();
		}
		if (e.getActionCommand() == "play") {
			// System.out.println("play!!");
			createGame();
		}

		if (e.getActionCommand() == "realisticColl") {
			JCheckBoxMenuItem c = (JCheckBoxMenuItem) e.getSource();
			if (c.isSelected())
				GameManager.realisticCollisions = true;
			else
				GameManager.realisticCollisions = false;
			System.out.println("real=" + GameManager.realisticCollisions);
		}
	}

	public boolean checkIfMouseIsWithinAGameObject(MouseEvent e) {
		boolean detected = false;
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject g = gameObjects.get(i);
			if (g.getCollider().getColliderShape() instanceof Circle) {
				Vector2d mousePos = new Vector2d(e.getX(), e.getY());
				// System.out.println("mouse"+mousePos);
				Circle c = ((Circle) g.getCollider().getColliderShape());
				// System.out.println("center"+c.getReferencePoint());
				Vector2d corrected = new Vector2d(c.getReferencePoint().getX() + c.getRadius(),
						c.getReferencePoint().getY() + c.getRadius() * 4);
				double dis = Vector2d.findDistanceBetweenTwoVector2ds(c.getReferencePoint(), mousePos);
				// System.out.println("dis"+dis);
				if (dis <= c.getRadius()) {
					outliner.updateSelected(g);
					inspector.setGameObject(g);
					detected = true;
				}
			}
			if (g.getCollider().getColliderShape() instanceof Rectangle) {
				Vector2d mousePos = new Vector2d(e.getX(), e.getY());
				Rectangle r = ((Rectangle) g.getCollider().getColliderShape());
				Vector2d AB = r.topSideVector, BC = r.rightSideVector, AM = Vector2d.sub(r.topLeftPoint, mousePos),
						BM = Vector2d.sub(r.topRightPoint, mousePos);
				if (0 <= Vector2d.dotProduct(AB, AM) && Vector2d.dotProduct(AB, AM) <= Vector2d.dotProduct(AB, AB)
						&& 0 <= Vector2d.dotProduct(BC, BM)
						&& Vector2d.dotProduct(BC, BM) <= Vector2d.dotProduct(BC, BC)) {
					outliner.updateSelected(g);
					inspector.setGameObject(g);
					detected = true;
				}
			}
		}
		if (!detected) {
			inspector.removeComponents();
			// outliner.clearSelection = true;
			outliner.selectedObject = null;
			outliner.buttonsList.clearSelection();
			System.out.println(outliner.buttonsList.isSelectionEmpty());
			// outliner.clearSelection = false;
			outliner.repaint();
			inspector.gameObject = null;
			movingAnObject = false;
		} else {
			grabFocus();
		}

		repaint();
		outliner.repaint();
		return detected;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		checkIfMouseIsWithinAGameObject(e);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	Vector2d mousePosWhenPressed = Vector2d.zero();
	Vector2d deltaPos;

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mousePosWhenPressed = new Vector2d(e.getX(), e.getY());
		boolean check = checkIfMouseIsWithinAGameObject(e);
		GameObject g = inspector.gameObject;

		if (check && g != null) {
			deltaPos = Vector2d.sub(g.getPosition(), mousePosWhenPressed);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		movingAnObject = false;
	}

	boolean movingAnObject = false;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		GameObject g = inspector.gameObject;
		if (!movingAnObject) {
			checkIfMouseIsWithinAGameObject(e);
			g = inspector.gameObject;
			if (g == null)
				return;
		}
		Vector2d mousePos = new Vector2d(e.getX(), e.getY());

		if (movingAnObject) {
			// g.getCollider().updatePosition(Vector2d.sub(deltaPos,g.getPosition()));
			// g.getCollider().updatePosition(Vector2d.add(deltaPos,Vector2d.sub(mousePos,g.getPosition())));
			// g.getCollider().updatePosition(Vector2d.sub(g.getCollider().getColliderShape().getReferencePoint(),Vector2d.add(deltaPos,mousePos)));
			g.getCollider().moveToPosition(Vector2d.add(deltaPos, mousePos));
			// g.getCollider().updatePosition(Vector2d.sub(mousePos,g.getPosition()));
			repaint();
			inspector.updateValues(g);
			return;
		}
		if (g.getCollider().getColliderShape() instanceof Circle) {
			// System.out.println("mouse"+mousePos);
			Circle c = ((Circle) g.getCollider().getColliderShape());
			// System.out.println("center"+c.getReferencePoint());
			Vector2d corrected = new Vector2d(c.getReferencePoint().getX() + c.getRadius(),
					c.getReferencePoint().getY() + c.getRadius() * 4);
			double dis = Vector2d.findDistanceBetweenTwoVector2ds(c.getReferencePoint(), mousePos);
			// System.out.println("dis"+dis);
			if (dis <= c.getRadius()) {
				movingAnObject = true;
				System.out.println("drag");
				// g.getCollider().updatePosition(Vector2d.sub(mousePos,g.getPosition()));
				// g.getCollider().updatePosition(Vector2d.sub(deltaPos,g.getPosition()));
				// g.getCollider().updatePosition(Vector2d.add(deltaPos,Vector2d.sub(mousePos,g.getPosition())));
				// g.getCollider().updatePosition(Vector2d.sub(c.getReferencePoint(),Vector2d.add(deltaPos,mousePos)));
				g.getCollider().moveToPosition(Vector2d.add(deltaPos, mousePos));
				repaint();
				inspector.updateValues(g);

				// inspector.repaint();
			}
		}
		if (g.getCollider().getColliderShape() instanceof Rectangle) {
			Rectangle r = ((Rectangle) g.getCollider().getColliderShape());
			Vector2d AB = r.topSideVector, BC = r.rightSideVector, AM = Vector2d.sub(r.topLeftPoint, mousePos),
					BM = Vector2d.sub(r.topRightPoint, mousePos);
			if (0 <= Vector2d.dotProduct(AB, AM) && Vector2d.dotProduct(AB, AM) <= Vector2d.dotProduct(AB, AB)
					&& 0 <= Vector2d.dotProduct(BC, BM) && Vector2d.dotProduct(BC, BM) <= Vector2d.dotProduct(BC, BC)) {
				// g.getCollider().updatePosition(Vector2d.sub(mousePos,g.getPosition()));
				// g.getCollider().updatePosition(Vector2d.add(deltaPos,Vector2d.sub(mousePos,g.getPosition())));
				// g.getCollider().updatePosition(Vector2d.sub(r.getReferencePoint(),Vector2d.add(deltaPos,mousePos)));
				g.getCollider().moveToPosition(Vector2d.add(deltaPos, mousePos));
				inspector.updateValues(g);
				movingAnObject = true;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		outliner.deleteGameObjectOnKeyPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
