package model.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.Solver;

public class SolverTimerTest {

	@Test
	public void time() throws IOException {
		FileReader f = new FileReader("puzzles/top95.txt");
		BufferedReader b = new BufferedReader(f);
		
		Solver[] solvers = new Solver[95];
		
		int n = 0;
		
		while (b.ready()) {
			String line = b.readLine();
			
			StringBuilder sb = new StringBuilder();
			sb.append("3\n");
			
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 8; j++) {
					sb.append(line.charAt(i*9 + j));
					sb.append(";");
				}
				sb.append(line.charAt(i*9+8));
				
				sb.append('\n');
			}

//			System.out.println(sb.toString());
			
			solvers[n] = new Solver(new Grid(sb.toString()));
			n++;
		}
		
		// WARM UP
		
		for (int iteration = 0; iteration < 100; iteration++) {
			for (int i = 0; i < solvers.length; i++) {
				assertTrue(solvers[i].solve() != null);
			}
		}
		
		long startTime = System.nanoTime();
		
		for (int iteration = 0; iteration < 100; iteration++) {
			for (int i = 0; i < solvers.length; i++) {
				assertTrue(solvers[i].solve() != null);
			}
		}
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
		System.out.println(duration);
	}
	
	// 1.34 with X-Wing and Twins
	// 1.29 with X-Wing
	// 1.13 with only UniqueCandidate
	
	// 1.16 without X-Wing
	// 1.3 with X-Wing
	
	/* AVERAGE RUNNING TIMES PER ITERATION FOR 100 ITERATIONS
	 * 
	 * IncrementalTwins + Unique: 2.0 ms
	 * Unique: 2.2
	 * Pure box/row/col: 11.7 ms
	 */
	
}
