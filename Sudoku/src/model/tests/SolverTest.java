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
	public void solveExample() throws IOException {
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
		
		assertFalse(grid.isSolved());
		
		Solver solver = new Solver(grid);
		
		Grid solved = solver.solve();
		
		assertFalse(solved == null);
		assertTrue(solved.isSolved());
	}
	
	public void solveSudoku(String filename) throws IOException {
		Grid g = new Grid(new FileReader("puzzles/" + filename + ".txt"));
		
		assertFalse(g.isSolved());
		
		Solver solver = new Solver(g);
		Grid solved = solver.solve();
		
		assertFalse(solved == null);
		
		if (!solved.isSolved()) {
			System.out.println(filename);
			System.out.println(solved);
		}
		
		assertTrue(solved.isSolved());
	}
	
	@Test
	public void solveSudoku1() throws IOException {
		solveSudoku("sudoku1");
	}
	
	@Test
	public void solveSudoku2() throws IOException {
		solveSudoku("sudoku2");
	}
	
	@Test
	public void solveSudoku3() throws IOException {
		solveSudoku("sudoku3");
	}
	
	@Test
	public void solveSudoku4() throws IOException {
		solveSudoku("sudoku4");
	}
	
	@Test
	public void solveSudoku5() throws IOException {
		solveSudoku("sudoku5");
	}
	
	@Test
	public void solveSudoku6() throws IOException {
		solveSudoku("sudoku6");
	}
	
	@Test
	public void solveEvil1() throws IOException {
		solveSudoku("sudoku_evil1");
	}

	@Test
	public void solveEvil2() throws IOException {
		solveSudoku("sudoku_evil2");
	}
	
	@Test
	public void solveEvil3() throws IOException {
		solveSudoku("sudoku_evil3");
	}
	
	@Test
	public void solveEvil4() throws IOException {
		solveSudoku("sudoku_evil4");
	}
	
	@Test
	public void solveExtreme1() throws IOException {
		solveSudoku("sudoku_extreme1");
	}

	@Test
	public void solveWorldsHardest() throws IOException {
		// From http://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html
		
		solveSudoku("sudoku_hardest_telegraph");
	}
	
	@Test
	public void solveNorvigHardest() throws IOException {
		solveSudoku("sudoku_norvig_hardest");
	}
	
//	@Test
	public void solveNorvigImpossible() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/sudoku_norvig_impossible.txt"));
		assertFalse(g.isSolved());
		Grid solved = new Solver(g).solve();
		assertTrue(solved == null);
	}
	
	@Test
	public void solveTetradoku1() throws IOException {
		solveSudoku("tetradoku1");
	}
	
	@Test
	public void solveTetradoku2() throws IOException {
		solveSudoku("tetradoku2");
	}
	
//	@Test
	public void solveTetradoku3() throws IOException {
		solveSudoku("tetradoku3");
	}
	
//	@Test
	public void solvePentadoku1() throws IOException {
		solveSudoku("pentadoku1");
	}
	
}
