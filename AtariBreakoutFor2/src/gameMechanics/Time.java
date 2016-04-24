package gameMechanics;

public class Time implements Runnable{

	public static double timeScale = 1, deltaTime = 1,timeSinceStart = 0;
	private static boolean endThread = false;
	public Time(){
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(!endThread){
//			double lastTime = System.nanoTime();
			timeSinceStart+=deltaTime;
			
			try {
				Thread.sleep((long) deltaTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			double fps = 1000000.0 / (lastTime - (lastTime = System.nanoTime())); 
//			System.out.println(fps);
			
		}
	}
	public static void terminate() {
		// TODO Auto-generated method stub
		endThread = true;	
	}
	
	public static void reset(){
		endThread = false;
		timeSinceStart = 0;
	}
	
}
