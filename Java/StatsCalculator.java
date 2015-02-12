package javaParserCompiler;

import java.util.ArrayList;

public class StatsCalculator {

	private double sum(ArrayList<Double> al) {
		double a = 0;
		for (double val : al) {
			a = a + val;
		}
		return a;
	}
	
	private ArrayList<Double> mean_sq(ArrayList<Double> al){
		ArrayList<Double> mn_sq=new ArrayList<Double>();
		double avg=average(al);
		for (double val:al){
			mn_sq.add(Math.pow(val-avg, 2));
		}
		return mn_sq;
	}

	public double average(ArrayList<Double> al) {
		return sum(al) / al.size();
	}

	public double standard_dev(ArrayList<Double> al) {
		return Math.pow(sum(mean_sq(al))/al.size(),0.5);
	}
	
	
}
