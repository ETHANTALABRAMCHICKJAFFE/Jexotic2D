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
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gameMechanics.Circle;
import gameMechanics.Rectangle;
import gamePieces.GameObject;
import math.Vector2d;

public class Inspector extends JPanel implements KeyListener, ActionListener{
	
	GameObject gameObject; // The GameObject being inspected
	JLabel xPositionLabel,yPositionLabel,positionLabel,scaleLabel,widthLabel,heightLabel,xVelocityLabel,yVelocityLabel,velocityLabel,massLabel;
	JTextField xPosition,yPosition,nameLabel,widthField,heightField,xVelocityField,yVelocityField,massField;
	MainEditor gamePanel;
	JCheckBox isMovable,isTrigger,isDestroyed,destroyOnCollision;
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
	
	public void addComponents(){
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		this.add(nameLabel,c);
		c.gridy = 1;
		c.weighty = 0;
		this.add(positionLabel,c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xPositionLabel,c);
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xPosition,c);
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yPositionLabel,c);
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yPosition,c);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 0;
		this.add(scaleLabel,c);
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 0;
		this.add(widthLabel, c);
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 0;
		this.add(widthField, c);
		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 0;
		this.add(heightLabel, c);
		c.gridx = 3;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 0;
		this.add(heightField, c);
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1;
		c.weighty = 0;
		this.add(velocityLabel,c);
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xVelocityLabel, c);
		c.gridx = 1;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 0;
		this.add(xVelocityField, c);
		c.gridx = 2;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yVelocityLabel, c);
		c.gridx = 3;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = 0;
		this.add(yVelocityField, c);
		c.gridx = 0;
		c.gridy = 7;
		c.weightx = 1;
		c.weighty = 0;
		this.add(massLabel, c);
		c.gridx = 0;
		c.gridy = 8;
		c.weightx = 1;
		c.weighty = 0;
		this.add(massField, c);
		
		c.gridx = 0;
		c.gridy = 9;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isMovable, c);
		c.gridx = 0;
		c.gridy = 10;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isTrigger, c);
		c.gridx = 0;
		c.gridy = 11;
		c.weightx = 1;
		c.weighty = 0;
		this.add(isDestroyed, c);
		c.gridx = 0;
		c.gridy = 12;
		c.weightx = 1;
		c.weighty = 1;
		this.add(destroyOnCollision, c);
		
		revalidate();
		repaint();
	}
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
		
		revalidate();
		repaint();
	}
	
	public void setGameObject(GameObject g){
		gameObject = g;
		//position.setText(g.getPosition().toString());
		//System.out.println("inspectorG"+gameObject);
		updateValues(g);
		//showElements();
	}
	
	public void updateValues(GameObject g){
		double x = g.getPosition().getX();
		x = Math.floor(x * 100) / 100;
		double y = g.getPosition().getY();
		y = Math.floor(y * 100) / 100;
		addComponents();
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
				((Circle)gameObject.getCollider().getColliderShape()).setRadius(height/2);
				gameObject.getCollider().getColliderShape().setShapeWidth(height);
				gameObject.getCollider().getColliderShape().setShapeHeight(height);
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
	}
}
