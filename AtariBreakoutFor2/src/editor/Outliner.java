package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gamePieces.GameObject;

public class Outliner extends JPanel implements ActionListener,KeyListener{
//todo a class that displays all current gameObjects
	JScrollPane objectList;
	ArrayList<GameObject> gameObjects;
	ArrayList<JButton> buttons;
	GameObject selectedObject;
	MainEditor m;
	JPanel buttonPanel;
	JList<String> buttonsList;
	DefaultListModel listModel;
	boolean clearSelection = false;
	public Outliner(MainEditor m){
		gameObjects = new ArrayList<GameObject>();
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		//buttonPanel.setPreferredSize(new Dimension(this.getSize().width, this.getSize().height))
		//String[] values = {"a","b","c","d"};

        listModel=new DefaultListModel();
        buttonsList=new JList(listModel);
        buttonsList.setBackground(Color.gray);
        buttonsList.setForeground(Color.black);
        buttonsList.setFont(buttonsList.getFont().deriveFont(Font.BOLD,15));
		buttonsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting() && !clearSelection){
		            JList source = (JList)e.getSource();
		            if(source.getSelectedValue() == null)
		            	return;
		            String selected = source.getSelectedValue().toString();
		            for (int i = 0; i < gameObjects.size(); i++) {
						if(selected == gameObjects.get(i).getName()){
							System.out.println(gameObjects.get(i).getName());
							selectedObject = gameObjects.get(i);
							m.inspector.setGameObject(selectedObject);	
							m.repaint();
							m.inspector.repaint();
						}
							
					}
		        }
			}
		});
		objectList = new JScrollPane(buttonsList);
        objectList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonsList.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				deleteGameObjectOnKeyPress(e);
			}
		});
//        buttonsList.setDragEnabled(true);
//        buttonsList.setDropMode(DropMode.INSERT);
        add(objectList);
        this.m = m;
        buttons = new ArrayList<JButton>();
        setLayout(new BorderLayout());
        add(objectList, BorderLayout.CENTER);
        //addKeyListener(this);
	}
	
	
	public void deleteGameObjectOnKeyPress(KeyEvent e){
		System.out.println("delg");
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
			System.out.println("delete");
			//buttonsList.remove(buttonsList.getSelectedIndex());
			GameObject g = m.inspector.gameObject;
			List<String> values = buttonsList.getSelectedValuesList();
			for (int i = 0; i<gameObjects.size();i++) {
				for (int j = 0; j < values.size(); j++) {
					if(gameObjects.get(i).getName() == values.get(j)){
					gameObjects.remove(i);
					m.gameObjects.remove(i);
					}
				}
				
			}
			//gameObjects.removeAll();
			//gameObjects.remove();
			createButtons();
			buttonsList.clearSelection();
//			m.gameObjects.remove(buttonsList.getSelectedIndices());
			m.inspector.gameObject = null;
			m.inspector.removeComponents();
			m.inspector.repaint();
			m.repaint();
			buttonsList.revalidate();
			buttonsList.repaint();
			
			repaint();
		}
	}
	public void updateSelected(GameObject g){
		String name = g.getName();
		for (int i = 0; i < gameObjects.size(); i++) {
			if(name == gameObjects.get(i).getName()){
				
				System.out.println(gameObjects.get(i).getName());
				//selectedObject = gameObjects.get(i);
				buttonsList.setSelectedIndex(i);
				System.out.println("selected"+i);
				//m.inspector.setGameObject(selectedObject);
				buttonsList.repaint();
				m.repaint();
				m.inspector.repaint();
				repaint();
				}
				
		}
	}
	public void addGameObject(GameObject g){
		gameObjects.add(g);
		selectedObject = g;
		
	}
	
	public void removeButtons(){
		for (int i = 0; i < buttons.size(); i++) {
			buttonPanel.remove(buttons.get(i));
		}
		buttons.clear();
	}
	public void createButtons(){
//		String[] values = new String[gameObjects.size()];
//		for (int i = 0; i < gameObjects.size(); i++) {
//			GameObject g = gameObjects.get(i);
//			values[i] = g.getName();
//		}
//		System.out.println(values);
//		buttonsList = new JList<String>(values);
		
	listModel.clear();
		for (int i=0; i<gameObjects.size(); i++) {
		  listModel.addElement(gameObjects.get(i).getName());
		}
//		buttonsList=new JList(listModel);
		buttonsList.repaint();
		objectList.repaint();
		repaint();
		
	}
	
	public GameObject getSelectedObject(){
		return selectedObject;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < buttons.size(); i++) {
			
			if(e.getActionCommand() == "g"+i){
				selectedObject = gameObjects.get(i);
				m.inspector.setGameObject(selectedObject);
				//JButton clicked = ((JButton)e.getSource());
				updateClickedButton(buttons.get(i));
				//return;
			}else{
				//JButton b = ((JButton)e.getSource());
				updateNotClickedButton(buttons.get(i));
			}
		}
		
	}
	public void updateClickedButton(JButton b){
		b.setForeground(Color.white);
		b.setBackground(Color.gray);
	}
	public void updateNotClickedButton(JButton b){
		b.setForeground(Color.black);
		b.setBackground(Color.lightGray);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("pressed");
		//deleteGameObjectOnKeyPress(e);
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
