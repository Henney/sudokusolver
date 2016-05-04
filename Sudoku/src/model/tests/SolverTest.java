package model.tests;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.Solver;

public class SolverTest {

	@Test
	public void findPossible3() throws IOException {
		String input =
				"3\n" +
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;9;5;.;2;." + "\n" +
				"4;.;.;.;.;.;1;.;." + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;4;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		

		Grid grid = new Grid(input);
		
		Solver solver = new Solver(grid);
		
		PossibleValues[] pvs = solver.findPossibleValues();
		
		// TODO: verify this
		Integer[][] expected = {
				{ 2 }, null, { 6 }, null, { 4 }, { 2, 7 }, null, { 9 }, { 4, 5, 9 },
				null, { 3, 8 }, null, null, { 1, 4 }, { 2, 8 }, null, { 3 }, { 1, 4 },
				null, { 3, 6, 8 }, null, { 1, 8 }, null, null, { 3, 6 }, null, { 1 },
				
				null, { 3, 6, 7, 9 }, { 5, 6, 7 }, { 5, 8, 9 }, { 3, 5, 6 }, { 3, 6, 8 }, null, { 7, 8 }, { 2, 8 },
				{ 9 }, null, null, { 5, 9 }, null, null, { 4 }, null, null,
				{ 1, 3 }, { 3, 6, 7 }, { 1, 6, 7 }, null, { 3, 6 }, null, null, null, { 8 },
				
				null, { 4, 8 }, null, { 1, 5 }, { 1, 5 }, null, { 2 }, { 8 }, null,
				{ 8, 9 }, { 7, 8, 9 }, { 7 }, null, null, { 3, 7 }, null, null, null,
				{ 1, 9 }, null, null, { 1, 7 }, null, { 3, 6, 7 }, { 3 }, null, { 9 }
		};
		
		for (int row = 0; row < grid.size(); row++) {
			for (int col = 0; col < grid.size(); col++) {
				int i = row*grid.size() + col;
				
				if (pvs[i] == null) {
					assert(expected[i] == null);
				} else {
					assertEquals(pvs[i].toSet(), new HashSet<Integer>(Arrays.asList(expected[i])));
				}
			}
		}	
	}
	
	@Test
	public void solveExample3() throws IOException {
		String input =
				"3\n" +
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;9;5;.;2;." + "\n" +
				"4;.;.;.;.;.;1;.;." + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;4;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		

		Grid grid = new Grid(input);
		
		Solver solver = new Solver(grid);
		
		Grid solved = solver.solve();
		
		System.out.println(solved);
	}
	
	@Test
	public void solveSudoku1() throws IOException {
		FileReader f = new FileReader("puzzles/sudoku6.txt");
		Solver solver = new Solver(new Grid(f));
		Grid solved = solver.solve();
		System.out.println(solved);
	}
	
}
