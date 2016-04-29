package gameMechanics;

public class FPSCounter extends Thread{
    private long lastTime;
    private double fps; //could be int or long for integer values
    private double minFPS;
    private boolean firstTime = true;
    private double sumFPS = 0,avgFPS = 0,counter = 0;
    private static boolean endThread = false;
    public void run(){
        while (!endThread){//lazy me, add a condition for an finishable thread
            lastTime = System.nanoTime();
            try{
                Thread.sleep(1000); // longer than one frame
            }
            catch (InterruptedException e){

            }
            
            //double prevFPS = fps;
            fps = 1000000000.0 / (System.nanoTime() - lastTime); //one second(nano) divided by amount of time it takes for one frame to finish
            sumFPS += fps;
            counter++;
            avgFPS = sumFPS/counter;
            if(firstTime){
            	minFPS = fps;
            	firstTime = false;
            }
            minFPS = Math.min(fps, minFPS);
            lastTime = System.nanoTime();
            System.out.println("FPS: "+fps);
            //System.out.println("avgFPS: "+avgFPS);
            //System.out.println("MinFPS: "+minFPS);
        }
    }
    public double fps(){
        return fps;
    } 
    public static void terminate(){
    	endThread = true;
    }
    public static void reset(){
    	endThread = false;
    }
}
