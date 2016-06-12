package editor;
import editor.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gameMechanics.Circle;
import gameMechanics.Rectangle;
import gamePieces.GameObject;
import math.Vector2d;

/**
 * @author Ethan
 * The inspector is the panel that displays the selected GameObject's information.
 */
public class Inspector extends JPanel implements KeyListener, ActionListener{
	
	GameObject gameObject; // The GameObject being inspected
	JLabel xPositionLabel,yPositionLabel,positionLabel,scaleLabel,widthLabel,heightLabel,xVelocityLabel,yVelocityLabel,velocityLabel,massLabel,
	rotationLabel;
	JTextField xPosition,yPosition,nameLabel,widthField,heightField,xVelocityField,yVelocityField,massField,
	rotationField;
	ArrayList<JLabel> scripts;
	MainEditor gamePanel;
	JCheckBox isMovable,isTrigger,isDestroyed,destroyOnCollision;
	JComboBox<String> addScriptButton;
	boolean hide = true;
	
	public Inspector(MainEditor e){
		super();
		
		setLayout(new GridBagLayout());
		setBackground(Color.darkGray);
		xPositionLabel = new JLabel("X");
		xPositionLabel.setForeground(Color.white);
		xPositionLabel.setFont(new Font(xPositionLabel.getName(),Font.PLAIN,20));

		yPositionLabel = new JLabel("Y");
		yPositionLabel.setForeground(Color.white);
		yPositionLabel.setFont(new Font(yPositionLabel.getName(),Font.PLAIN,20));
		
		positionLabel = new JLabel("Position:");
		positionLabel.setForeground(Color.white);
		positionLabel.setFont(new Font(positionLabel.getName(),Font.PLAIN,30));
		
		nameLabel = new JTextField();
		nameLabel.setFont(new Font(nameLabel.getName(),Font.PLAIN,40));
		nameLabel.setActionCommand("name");
		nameLabel.addActionListener(this);
		
		scaleLabel = new JLabel("Scale:");
		scaleLabel.setForeground(Color.white);
		scaleLabel.setFont(new Font(scaleLabel.getName(),Font.PLAIN,30));
		
		
		widthLabel = new JLabel("w");
		widthLabel.setForeground(Color.white);
		widthLabel.setFont(new Font(widthLabel.getName(),Font.PLAIN,20));
		
		heightLabel = new JLabel("h");
		heightLabel.setForeground(Color.white);
		heightLabel.setFont(new Font(heightLabel.getName(),Font.PLAIN,20));
		
		velocityLabel = new JLabel("Velocity:");
		velocityLabel.setForeground(Color.white);
		velocityLabel.setFont(new Font(velocityLabel.getName(),Font.PLAIN,30));
		
		
		xVelocityLabel = new JLabel("X");
		xVelocityLabel.setForeground(Color.white);
		xVelocityLabel.setFont(new Font(xVelocityLabel.getName(),Font.PLAIN,20));
		
		yVelocityLabel = new JLabel("Y");
		yVelocityLabel.setForeground(Color.white);
		yVelocityLabel.setFont(new Font(yVelocityLabel.getName(),Font.PLAIN,20));
		
		massLabel = new JLabel("Mass:");
		massLabel.setForeground(Color.white);
		massLabel.setFont(new Font(massLabel.getName(),Font.PLAIN,30));
		
		massField = new JTextField(5);
		massField.setHorizontalAlignment(SwingConstants.LEFT);
		massField.setActionCommand("mass");
		massField.addActionListener(this);
		xPosition = new JTextField(5);
		yPosition = new JTextField(5);
		xPosition.setHorizontalAlignment(SwingConstants.LEFT);
		yPosition.setHorizontalAlignment(SwingConstants.LEFT);
		xPosition.setActionCommand("x");
		yPosition.setActionCommand("y");
		xPosition.addActionListener(this);
		yPosition.addActionListener(this);
		
		widthField = new JTextField(5);
		heightField = new JTextField(5);
		widthField.setHorizontalAlignment(SwingConstants.LEFT);
		heightField.setHorizontalAlignment(SwingConstants.LEFT);
		widthField.setActionCommand("w");
		heightField.setActionCommand("h");
		widthField.addActionListener(this);
		heightField.addActionListener(this);
		
		xVelocityField = new JTextField(5);
		yVelocityField = new JTextField(5);
		xVelocityField.setHorizontalAlignment(SwingConstants.LEFT);
		yVelocityField.setHorizontalAlignment(SwingConstants.LEFT);
		xVelocityField.setActionCommand("xVel");
		yVelocityField.setActionCommand("yVel");
		xVelocityField.addActionListener(this);
		yVelocityField.addActionListener(this);
		
		
		rotationLabel = new JLabel("Rotation:");
		rotationLabel.setForeground(Color.white);
		rotationLabel.setFont(new Font(rotationLabel.getName(),Font.PLAIN,30));
		
		rotationField = new JTextField(5);
		rotationField.setHorizontalAlignment(SwingConstants.LEFT);
		rotationField.setActionCommand("rotation");
		rotationField.addActionListener(this);
		
		addScriptButton = new JComboBox<String>();
		addScriptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (addScriptButton.getSelectedItem() != null) {

					String scriptName = addScriptButton.getSelectedItem().toString();
					for (int j = 0; gameObject.getScripts() != null && gameObject.getScripts().size() > 0 && j < gameObject.getScripts().size(); j++) {
						String scriptString = gameObject.getScripts().get(j).getClass().getName();
						System.out.println(scriptString);
						scriptString = scriptString.substring(8);
						//scriptString = scriptString.substring(0, scriptString.indexOf(".java"));
						System.out.println(scriptString);
						if(scriptString.equals(scriptName))
							return;	
					}
					System.out.println("add");
					gameObject.addScriptFile(addScriptButton.getSelectedItem().toString());
					updateValues(gameObject);
				}
			}
		});
		isMovable = new JCheckBox("Is Movable");
		isMovable.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean movable = isMovable.isSelected();
				gameObject.setMovable(movable);
			}
		});
		
		isTrigger = new JCheckBox("Collider is trigger");
		isTrigger.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean trigger = isTrigger.isSelected();
				gameObject.setTrigger(trigger);
			}
		});
		isDestroyed = new JCheckBox("Is Destroyed");
		isDestroyed.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean destroyed = isDestroyed.isSelected();
				gameObject.setIsDestroyed(destroyed);
			}
		});
		destroyOnCollision = new JCheckBox("Destroy on collision");
		destroyOnCollision.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean destroy = destroyOnCollision.isSelected();
				gameObject.setDestroyOnCollision(destroy);
				System.out.println(gameObject.isDestroyOnCollision());
			}
		});
		isDestroyed.setForeground(Color.white);
		isDestroyed.setBackground(this.getBackground());
		isTrigger.setForeground(Color.white);
		isTrigger.setBackground(this.getBackground());
		isMovable.setForeground(Color.white);
		isMovable.setBackground(this.getBackground());
		destroyOnCollision.setForeground(Color.white);
		destroyOnCollision.setBackground(this.getBackground());
		gamePanel = e;
		
		
	}
	
	
	/**
	 * adds all the components that display the information on the selected GameObject. 
	 */
	public void addComponents(){
		int y = 0;
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 4;
		this.add(nameLabel,c);
		y++;
		c.gridy = y;
		c.weighty = 0;
		this.add(positionLabel,c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xPositionLabel,c);
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xPosition,c);
		c.gridx = 2;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yPositionLabel,c);
		c.gridx = 3;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yPosition,c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(scaleLabel,c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(widthLabel, c);
		y++;
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(widthField, c);
		c.gridx = 2;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(heightLabel, c);
		c.gridx = 3;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(heightField, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(rotationLabel,c);
		y++;
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(rotationField, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(velocityLabel,c);
		y++; 
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xVelocityLabel, c);
		System.out.println(y);
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xVelocityField, c);
		c.gridx = 2;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yVelocityLabel, c);
		c.gridx = 3;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yVelocityField, c);

		System.out.println("scriptssize"+scripts.size());
		for (int i = 0; scripts != null && scripts.size() > 0 && i < scripts.size(); i++) {
			y+=3;
			c.gridx = 0;
			c.gridy = y;
			c.weightx = 1;
			c.weighty = 0;
			this.add(scripts.get(i),c);
			
		}
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(massLabel, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(massField, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isMovable, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isTrigger, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isDestroyed, c);
		y++;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 1;
		this.add(destroyOnCollision, c);
		y++;

		c.gridx = 0;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 1;
		//addScriptButton.removeAllItems();
		File root = new File(ProjectManager.projectDirectory);
		File sourceFile = new File(root, "\\Scripts\\");
		File[] scriptList = sourceFile.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return pathname.getName().endsWith(".java");
			}
		});
		for (int j = 0; scriptList != null && scriptList.length > 0 && j < scriptList.length; j++) {
			addScriptButton.addItem(scriptList[j].getName().substring(0, scriptList[j].getName().indexOf(".java")));	
		}
		this.add(addScriptButton, c);
		revalidate();
		repaint();
	}
	
	/**
	 * removes all the components that display the information on the selected GameObject. 
	 */
	public void removeComponents(){
		this.remove(nameLabel);
		this.remove(positionLabel);
		this.remove(xPositionLabel);
		this.remove(yPositionLabel);
		this.remove(xPosition);
		this.remove(yPosition);
		this.remove(scaleLabel);
		this.remove(widthField);
		this.remove(heightField);
		this.remove(widthLabel);
		this.remove(heightLabel);
		this.remove(velocityLabel);
		this.remove(xVelocityLabel);
		this.remove(yVelocityLabel);
		this.remove(xVelocityField);
		this.remove(yVelocityField);
		this.remove(massField);
		this.remove(massLabel);
		this.remove(isMovable);
		this.remove(isTrigger);
		this.remove(isDestroyed);
		this.remove(destroyOnCollision);
		this.remove(rotationField);
		this.remove(rotationLabel);
		for (int i = 0; scripts != null && scripts.size() > 0 && i < scripts.size(); i++) {
			this.remove(scripts.get(i));
			//scripts.remove(i);
		}
		
		this.remove(addScriptButton);
		addScriptButton.removeAll();
		revalidate();
		repaint();
	}
	
	
	/** sets the current selected GameObject
	 * @param g the GameObject that is set to.
	 */
	public void setGameObject(GameObject g){
		gameObject = g;
		//position.setText(g.getPosition().toString());
		//System.out.println("inspectorG"+gameObject);
		updateValues(g);
		//showElements();
	}
	
	/** updates the values of the selected GameObject.
	 * @param g the selected GameObject
	 */
	public void updateValues(GameObject g){
		double x = g.getPosition().getX();
		x = Math.floor(x * 100) / 100;
		double y = g.getPosition().getY();
		y = Math.floor(y * 100) / 100;
				
		xPosition.setText(x+"");
		yPosition.setText(y+"");
		nameLabel.setText(g.getName());
		widthField.setText(g.getCollider().getColliderShape().getShapeWidth()+"");
		heightField.setText(g.getCollider().getColliderShape().getShapeHeight()+"");
		double xVel = g.getVelocity().getX();
		xVel = Math.floor(xVel * 100 ) / 100;
		double yVel = g.getVelocity().getY();
		yVel = Math.floor(yVel * 100 ) / 100;
		xVelocityField.setText(xVel+"");
		yVelocityField.setText(yVel+"");
		double mass = g.getMass();
		mass = Math.floor(mass * 100) / 100;
		massField.setText(mass+"");
		isDestroyed.setSelected(g.getIsDestroyed());
		isMovable.setSelected(g.isMovable());
		isTrigger.setSelected(g.isTrigger());
		destroyOnCollision.setSelected(g.isDestroyOnCollision());
		double rotAng = g.getCollider().getColliderShape().getRotationAngle();
		rotAng = Math.floor(rotAng * 100) / 100;
		rotationField.setText(rotAng+"");
		
		scripts = new ArrayList<JLabel>();
		if(gameObject != null && gameObject.getScripts() != null)
		System.out.println("scriptssize1"+gameObject.getScripts().size());
		for (int i = 0;gameObject.getScripts() != null && gameObject.getScripts().size() > 0 &&  i < gameObject.getScripts().size(); i++) {
			JLabel scriptLabel = new JLabel(gameObject.getScripts().get(i).getClass().getName().substring(8));
			scriptLabel.setForeground(Color.white);
			scriptLabel.setFont(new Font(scriptLabel.getName(),Font.PLAIN,20));
			boolean contains = false;
			for (int j = 0; j < scripts.size(); j++) {
				if(scripts.get(j).getText() == scriptLabel.getText()){
					contains = true;
			}
			
			}
			if(!contains)
			scripts.add(scriptLabel);
		}
		//removeComponents();
		addComponents();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == "x"){
			double xPos = Double.parseDouble(xPosition.getText().trim());
			gameObject.getCollider().updatePosition(new Vector2d(xPos-gameObject.getPosition().getX(), 0));
			//gameObject.setPosition(new Vector2d(Double.parseDouble(xPosition.getText().trim()), gameObject.getPosition().getY()));
			gamePanel.repaint();
			//updateValues(gameObject);
		}
		if(e.getActionCommand() == "y"){
			double yPos = Double.parseDouble(yPosition.getText().trim());
			gameObject.getCollider().updatePosition(new Vector2d(0,yPos-gameObject.getPosition().getY()));
			gamePanel.repaint();
		}
		if(e.getActionCommand() == "xVel"){
			double xVel = Double.parseDouble(xVelocityField.getText().trim());
			gameObject.setVelocity(new Vector2d(xVel,gameObject.getVelocity().getY()));
		}
		if(e.getActionCommand() == "yVel"){
			double yVel = Double.parseDouble(yVelocityField.getText().trim());
			gameObject.setVelocity(new Vector2d(gameObject.getVelocity().getX(),yVel));
		}
		if(e.getActionCommand() == "w"){
			double width = Double.parseDouble(widthField.getText().trim());
			if(gameObject.getCollider().getColliderShape() instanceof Circle)
			{
				((Circle)gameObject.getCollider().getColliderShape()).setRadius(width/2);
				gameObject.getCollider().getColliderShape().setShapeWidth(width);
				gameObject.getCollider().getColliderShape().setShapeHeight(width);
				heightField.setText(width+"");
				gamePanel.repaint();
			}else if(gameObject.getCollider().getColliderShape() instanceof Rectangle){
				gameObject.getCollider().getColliderShape().setShapeWidth(width);
				Rectangle r = ((Rectangle)gameObject.getCollider().getColliderShape());
				//ArrayList<Vector2d> points = new ArrayList<Vector2d>(r.getPoints());
				r.setLengthOfSideB(width);
				r.setPoints(Rectangle.calculateRectanglePoints(r.getLengthOfSideA(), width, r.getReferencePoint(), r.getNumOfPoints()));
				r.rotate(r.getRotationAngle());
				r.topLeftPoint = r.getPoints().get(0);
				r.topRightPoint = r.getPoints().get(1);
				r.bottomRightPoint = r.getPoints().get(2);
				r.bottomLeftPoint = r.getPoints().get(3);
				//r.calculateCornersAndSides();
				//Rectangle.calculateSides(r);
				gamePanel.repaint();
				}
		}
		if(e.getActionCommand() == "h"){
			double height = Double.parseDouble(heightField.getText().trim());
			if(gameObject.getCollider().getColliderShape() instanceof Circle)
			{
				Circle c = (Circle)gameObject.getCollider().getColliderShape();
				c.setRadius(height/2);
				gameObject.getCollider().getColliderShape().setShapeWidth(height);
				gameObject.getCollider().getColliderShape().setShapeHeight(height);
				c.rotate(c.getRotationAngle());
				widthField.setText(height+"");
				gamePanel.repaint();
			}else if(gameObject.getCollider().getColliderShape() instanceof Rectangle){
			gameObject.getCollider().getColliderShape().setShapeHeight(height);
			Rectangle r = ((Rectangle)gameObject.getCollider().getColliderShape());
			r.setLengthOfSideA(height);
			r.setPoints(Rectangle.calculateRectanglePoints(height, r.getLengthOfSideB(), r.getReferencePoint(), r.getNumOfPoints()));
			r.rotate(r.getRotationAngle());
			r.topLeftPoint = r.getPoints().get(0);
			r.topRightPoint = r.getPoints().get(1);
			r.bottomRightPoint = r.getPoints().get(2);
			r.bottomLeftPoint = r.getPoints().get(3);
			//r.calculateCornersAndSides();
			//Rectangle.calculateSides(r);
			gamePanel.repaint();
			}
		}
		if(e.getActionCommand() == "name"){
			gameObject.setName(nameLabel.getText());
			gamePanel.outliner.createButtons();
			gamePanel.outliner.updateSelected(gameObject);
			gamePanel.outliner.revalidate();
			gamePanel.outliner.repaint();
			gamePanel.repaint();
		}
		if(e.getActionCommand() == "mass"){
			gameObject.setMass(Double.parseDouble(massField.getText().trim()));
			gamePanel.repaint();
			repaint();
		}
		if(e.getActionCommand() == "rotation"){
			double angle = Double.parseDouble(rotationField.getText().trim());
			if(gameObject.getCollider().getColliderShape() instanceof Circle)
			{
				gameObject.getCollider().getColliderShape().rotate(angle);
				rotationField.setText(angle+"");
				gamePanel.repaint();
			}else if(gameObject.getCollider().getColliderShape() instanceof Rectangle){
			//gameObject.getCollider().getColliderShape().rotate(angle);
			Rectangle r = ((Rectangle)gameObject.getCollider().getColliderShape());
			r.rotate(angle);
			gamePanel.repaint();
			}
		}
	}
}
