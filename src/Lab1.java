/**
 * 
 *CS 383 Fall 2015 - Programming Lab #1
 *
 */
public class Lab1 {

	public static void main(String[] args) {
		
		if (args.length < 1)
			throw new IllegalArgumentException("No data file provided");
		char [] vals = new In(args[0]).readAll().toCharArray();
		
		TuringSim mySim = new TuringSim();
		mySim.writeInput(vals);
		StdOut.printf("TuringSim  %s input file: %s\n", booleanToString(mySim.accepts()), args[0]);
		StdOut.printf("StaticTest %s input file: %s\n", booleanToString(StaticTest(vals)), args[0]);
	}
	
	public static String booleanToString(boolean v) {
		return (v) ? "Accepts" : "Rejects";
	}
	
	public static boolean StaticTest(char [] vals) {
		if (vals.length % 2 != 0) return false;
		
		int mid = vals.length/2;
		for(int i=0;i<mid;i++) {
			if (vals[i] != vals[mid+i]) return false;
		}
		return true;
	}
}
