package math;

import java.util.ArrayList;

public class NewMath {

	public static double calculateDistanceOfPointToLine(Vector2d p, ArrayList<Vector2d> l) {

		double[] abc = calculateEquationOfLine(l);
		double a = abc[0];
		double b = abc[1];
		double c = abc[2];
		double x0 = p.getX(), y0 = p.getY();
		//System.out.println("a= "+a+";b= "+b+";c= "+c);
		double dis = Math.abs(a * x0 + b * y0 + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		if(dis <= 50)
			System.out.println(dis);
		return dis;
	}

	public static double distanceBetweenPointAndLine(Vector2d point, ArrayList<Vector2d> l){
		double x0 = point.getX(),y0 = point.getY();
		double y1,x1,y2,x2;
		double dis = 1000000000;
		//if(l.get(0) == null)
		//System.out.println(l);
		//try{
		y1 = l.get(0).getY(); x1 = l.get(0).getX();
				y2 = l.get(l.size()-1).getY();x2 = l.get(l.size()-1).getX();
				dis = Math.abs((y1-y2)*x0+(x2-x1)*y0+(x1*y2-x2*y1))/Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
//		}
//		catch(IndexOutOfBoundsException e){
//			System.out.println(e);
//		}
		//distance formula
		
		//double dis = Math.abs((y1-y2)*x0+(x2-x1)*y0+(x1*y2-x2*y1))/Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
		return dis;
		
	}
	
	public static double clamp(double val,double min,double max){
		return Math.max(min, Math.min(max, val));
	}
	public static double[] calculateEquationOfLine(ArrayList<Vector2d> l) {
		// y-y1 = m(x-x1);
		Vector2d startPoint = l.get(0), endPoint = l.get(l.size() - 1);
		// y = m*x-m*x1+y1
		// Ax+By+c = 0
		// m*x-1*y+(y1-m*x1) = 0;
		double y1 = startPoint.getY(), y2 = endPoint.getY(), x1 = startPoint.getX(), x2 = endPoint.getX();
		if (x1 - x2 != 0) {
			double m = (y2 - y1) / (x2 - x1);
			//System.out.println(m);
			double a = m;
			double b = -1;
			double c = y1 - m * x1;
			double[] abc = { a, b, c };
			return abc;
		} else {
			double c = x1;
			double b = 0;
			double a = 1;
			double[] abc = { a, b, c };
			return abc;
		}
	}
}
