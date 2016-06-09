import gameMechanics.*;
import gamePieces.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import math.*;
import java.lang.Math;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
public class DemoScript1 implements GameBehavior{
		public static int score = 0;
		double angle = 0;
			@Override
			public void update(GameObject g) {
				// TODO Auto-generated method stub
				//setVelocity(new Vector2d(1,0));
				g.setColor(Color.orange);
//				GameObject copyGameObject = GameBehavior.createCopyGameObject(g);
//				copyGameObject.getCollider().moveToPosition(Vector2d.sub(g.getPosition(),g.getCollider().getColliderShape().getShapeWidth()));
				createTail(g);
				System.out.println("vel"+g.getVelocity());
//				if(Input.isKeyPressed(KeyEvent.VK_LEFT)){
//					g.getCollider().updatePosition(new Vector2d(5,0));
//				}
				if(Input.isKeyPressed(KeyEvent.VK_LEFT)){
					g.setVelocity(new Vector2d(-10,0));
				}
				if(Input.isKeyPressed(KeyEvent.VK_RIGHT)){
					g.setVelocity(new Vector2d(10,0));
				}
				if(Input.isKeyReleased(KeyEvent.VK_LEFT) && Input.isKeyReleased(KeyEvent.VK_RIGHT)){
					g.setVelocity(new Vector2d(0,0));
					
				}
				
				g.rotate(g.getCollider().getColliderShape().getRotationAngle()+angle);
				angle++;
			}
			public void createTail(GameObject g){
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true){		
							GameObject copyGameObject = GameBehavior.createCopyGameObject(g);
							
							copyGameObject.getCollider().moveToPosition(Vector2d.sub(g.getPosition(),new Vector2d(g.getCollider().getColliderShape().getShapeWidth(),0)));
							try{
							Thread.sleep(500);
							}catch(Exception e){
								
							}		
					}
				},"onCollision");
				
			}
			@Override
			public void restore(GameObject g) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTrigger(Collider other,GameObject g) {
				// TODO Auto-generated method stub
				GameObject otherObject = ((GameObject)other.getParent());
				otherObject.destroy(otherObject);
			}
			
			@Override
			public void onRestore(GameObject g) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDestroy(GameObject g) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCollision(Collider other,GameObject g) {
				// TODO Auto-generated method stub
				System.err.println("Score"+(++score));
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for(int i = 0; i < 10;i++){
							GameObject copyGameObject = GameBehavior.createGameObject("Rectangle", g.getPosition(), new Vector2d(Math.random()*5,Math.random()*5), 8, 8, 1, Color.red, false));
						}
					}
				});
				t.start();
			}
			@Override
			public void destroy(GameObject g) {
				// TODO Auto-generated method stub
				
			}
		}
