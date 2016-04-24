package gameMechanics;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

import math.Vector2d;

public class Input {
	private static ArrayList<Integer> currentKeyPressed, currentKeyReleased, currentKeyTyped;
	public Input() {
		currentKeyPressed = new ArrayList<Integer>();
		currentKeyReleased = new ArrayList<Integer>();
		Field[] fields = java.awt.event.KeyEvent.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers())) {
		    	if(f.getType() == int.class){
		        //System.out.println(f.getName());
		        try {
					currentKeyReleased.add(f.getInt(f));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	}
		    } 
		}
		currentKeyTyped = new ArrayList<Integer>();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				synchronized (Input.class) {
					switch (ke.getID()) {
					case KeyEvent.KEY_PRESSED:
						if(!currentKeyPressed.contains(ke.getKeyCode())){
//							System.out.println("ke.getKeyCode() = "+ ke.getKeyCode());
							currentKeyPressed.add(ke.getKeyCode());
							if(currentKeyReleased.contains(ke.getKeyCode())){
//								System.out.println("contain");
								currentKeyReleased.remove(new Integer(ke.getKeyCode()));
							}
						}
						//						if (currentKeyReleased.contains(ke.getKeyCode())){
						//							currentKeyReleased.remove(ke.getKeyCode());
						//						}
						break;

					case KeyEvent.KEY_RELEASED:
						if(!currentKeyReleased.contains(ke.getKeyCode())){
							currentKeyReleased.add(ke.getKeyCode());
							if(currentKeyPressed.contains(ke.getKeyCode())){
								currentKeyPressed.remove(new Integer(ke.getKeyCode()));
							}
						}
						//						if (currentKeyPressed.contains(ke.getKeyCode())){
						//							currentKeyPressed.remove(ke.getKeyCode());
						//						}
						break;
						//TODO: add case KeyEvent.KEY_TYPED.
					}
					return false;
				}
			}
		});
	}

	private static boolean wPressed = false;

	public static boolean isKeyPressed(int keyCode) {
		synchronized (Input.class) {
			for (Integer i : currentKeyPressed) {
				if (i == keyCode){
					//					currentKeyPressed.remove(i);
					return true;
				}
			}
			return false;
		}
	}
	public static boolean isKeyReleased(int keyCode) {
		synchronized (Input.class) {
			for (Integer i : currentKeyReleased) {
				if (i == keyCode){
					//currentKeyReleased.remove(i);
					return true;
				}
			}
			return false;
		}
	}

}
