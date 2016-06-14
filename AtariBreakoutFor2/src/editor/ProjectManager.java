package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gamePieces.GameObject;

/**
 * @author Ethan The ProjectManager is the class that opens a window that allows
 *         you to create and open new/existing projects, load their information
 *         and open the MainEditor.
 */
public class ProjectManager {

	public static String projectDirectory = "demo1";

	JList<String> buttonsList;
	DefaultListModel listModel;
	JScrollPane objectList;
	JFrame f;
	JButton createProject, openProject;

	public ProjectManager() {

		listModel = new DefaultListModel();
		findAllProjects();
		buttonsList = new JList(listModel);
		buttonsList.setBackground(Color.gray);
		buttonsList.setForeground(Color.black);
		buttonsList.setFont(buttonsList.getFont().deriveFont(Font.BOLD, 15));
		buttonsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!e.getValueIsAdjusting()) {
					JList source = (JList) e.getSource();
					if (source.getSelectedValue() == null)
						return;
					String selected = source.getSelectedValue().toString();
					loadProject(selected);
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
			}
		});
		// buttonsList.setDragEnabled(true);
		// buttonsList.setDropMode(DropMode.INSERT);
		f = new JFrame("Select Project");
		// ]f.setIconImage(new
		// ImageIcon(getClass().getResource("Jexotic2D/Images/JexoticIcon1.png")).getImage());
		Image icon = Toolkit.getDefaultToolkit().getImage("Images/JexoticIcon1.png");
		f.setIconImage(icon);
		// f.setIconImage((new
		// ImageIcon("Jexotic2D/Images/JexoticIcon1.png")).getImage());
		f.setBounds(250, 100, 1200, 800);
		JPanel p = new JPanel();

		p.add(objectList);
		p.setLayout(new BorderLayout());
		p.add(objectList, BorderLayout.CENTER);
		openProject = new JButton("Open Project");
		openProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser = new JFileChooser(new File("C:\\Jexotic2D Projects"));
				// int returnValue = fileChooser.showOpenDialog(null);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					loadProject(selectedFile.getName());
					System.out.println(selectedFile.getName());
				}

			}
		});
		// p.add(createProject, BorderLayout.NORTH);
		p.add(openProject, BorderLayout.SOUTH);
		f.add(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		// createProject("Demo1");
	}

	/**
	 * Finds all the projects that are within the C:\\Jexotic2D Projects folder.
	 */
	public void findAllProjects() {
		File file = new File("C:\\Jexotic2D Projects");
		String[] names = file.list();

		for (String name : names) {
			if (new File("C:\\Jexotic2D Projects\\" + name).isDirectory()) {
				listModel.addElement("C:\\Jexotic2D Projects\\" + name);
			}
		}
	}

	/**
	 * saves the project's current state and GameObject ArrayList.
	 * 
	 * @param m
	 *            the MainEditor
	 */
	public static void saveProject(MainEditor m) {

		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(projectDirectory + "\\game_objects.ser"));
			out.writeObject(m.gameObjects);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Loads the GameObject list of existing project saved in the file system.
	 * 
	 * @param dir
	 */
	public static void loadProject(String dir) {
		ArrayList<GameObject> g = null;
		try {
			Path path = Paths.get(dir + "\\game_objects.ser");
			if (Files.exists(path)) {

				ObjectInputStream in = new ObjectInputStream(new FileInputStream(dir + "\\game_objects.ser"));
				// if(in.readObject() instanceof ArrayList)
				Object b = in.readObject();
				if (b instanceof ArrayList)
					g = (ArrayList<GameObject>) b;
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		projectDirectory = dir;
		MainEditor editor = new MainEditor();
		editor.createGUI();
		if (g != null) {
			for (GameObject gameObject : g) {
				if (gameObject.getScriptPaths() != null && gameObject.getScriptPaths().size() > 0) {
					ArrayList<String> scripts = new ArrayList<String>(gameObject.getScriptPaths());
					gameObject.getScriptPaths().clear();
					for (int i = 0; i < scripts.size(); i++) {
						gameObject.addScriptFile(scripts.get(i));
					}
				}

				editor.outliner.addGameObject(gameObject);
				editor.outliner.createButtons();
				editor.outliner.updateSelected(gameObject);
			}
			editor.gameObjects = g;
		}
	}

	/**
	 * Creates the projects folder.
	 */
	public static void createProjectsFolder() {
		Path projectsFolder = Paths.get("C:\\Jexotic2D Projects");
		if (!Files.exists(projectsFolder)) {
			try {
				Files.createDirectories(projectsFolder);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a new Project.
	 * 
	 * @param name
	 *            the name of the new project.
	 */
	public static void createProject(String name) {

		Path path = Paths.get("C:\\Jexotic2D Projects\\" + name);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				MainEditor m = new MainEditor();
				m.createGUI();
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		createProjectsFolder();
		ProjectManager pm = new ProjectManager();
	}
}
