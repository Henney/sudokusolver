package model.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.Solver;
import model.TacticSolver;

public class TacticSolverTest {

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
		
		PossibleValues[] pvs = grid.findPossibleValues();
		
		pvs[0].toString();
		
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
	public void invalidBoard() throws IOException {
		String input = 
				"3\n" +
				"3;7;5;2;1;4;8;9;6;\n" +
				"1;2;8;6;9;3;4;7;5;\n" +
				"9;6;4;8;5;7;2;1;3;\n" +
				"8;5;2;3;4;1;7;6;9;\n" +
				"4;1;9;7;2;6;5;3;8;\n" +
				"7;3;6;9;8;5;1;4;2;\n" +
				"6;4;1;5;3;2;9;8;7;\n" +
				"5;8;3;1;7;9;6;2;7;\n" +
				"2;9;7;4;6;8;3;5;1;";
		
		Grid g = new Grid(input);
		assertFalse(g.isLegal());
		assertFalse(g.isSolved());
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
		
		TacticSolver solver = new TacticSolver(grid);
		
		Grid solved = solver.solve();
		
		assertFalse(solved == null);
		assertTrue(solved.isSolved());
	}
	
	public static void addTxtSolve(String filename) throws IOException {
		solveSudoku("puzzles/" + filename + ".txt");
	}
	
	public static void solveSudoku(String filename) throws IOException {
		Grid g = new Grid(new FileReader(filename));
		g.toString();
		
		assertFalse(g.isSolved());
		
		TacticSolver solver = new TacticSolver(g);
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
		addTxtSolve("sudoku1");
	}
	
	@Test
	public void solveSudoku2() throws IOException {
		addTxtSolve("sudoku2");
	}
	
	@Test
	public void solveSudoku3() throws IOException {
		addTxtSolve("sudoku3");
	}
	
	@Test
	public void solveSudoku4() throws IOException {
		addTxtSolve("sudoku4");
	}
	
	@Test
	public void solveSudoku5() throws IOException {
		addTxtSolve("sudoku5");
	}
	
	@Test
	public void solveSudoku6() throws IOException {
		addTxtSolve("sudoku6");
	}
	
	@Test
	public void solveEvil1() throws IOException {
		addTxtSolve("sudoku_evil1");
	}

	@Test
	public void solveEvil2() throws IOException {
		addTxtSolve("sudoku_evil2");
	}
	
	@Test
	public void solveEvil3() throws IOException {
		addTxtSolve("sudoku_evil3");
	}
	
	@Test
	public void solveEvil4() throws IOException {
		addTxtSolve("sudoku_evil4");
	}
	
	@Test
	public void solveExtreme1() throws IOException {
		addTxtSolve("sudoku_extreme1");
	}

	@Test
	public void solveWorldsHardest() throws IOException {
		// From http://www.telegraph.co.uk/news/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html
		
		addTxtSolve("sudoku_hardest_telegraph");
	}
	
	@Test
	public void solveNorvigHardest() throws IOException {
		addTxtSolve("sudoku_norvig_hardest");
	}
	
	public static void solveImpossible(String filename) throws FileNotFoundException, IOException {
		Grid g = new Grid(new FileReader(filename));
		assertFalse(g.isSolved());
		Grid solved = new TacticSolver(g).solve();
		assertTrue(solved == null);
	}
	
	@Test
	public void solveNorvigImpossible() throws IOException {
		solveImpossible("puzzles/sudoku_norvig_impossible.txt");
	}
	
	@Test
	public void solveTetradoku1() throws IOException {
		addTxtSolve("tetradoku1");
	}
	
	@Test
	public void solveTetradoku2() throws IOException {
		addTxtSolve("tetradoku2");
	}
	
	@Test
	public void solveTetradoku3() throws IOException {
		addTxtSolve("tetradoku3");
	}
	
	@Test
	public void solvePentadoku1() throws IOException {
		addTxtSolve("pentadoku1");
	}
	
	@Test
	public void solveHexadoku1() throws IOException {
		addTxtSolve("hexadoku1");
	}
	
	@Test
	public void solveGeneratedHexadoku() throws IOException {
		addTxtSolve("hexadokus/hexadoku1");
	}
	
//	@Test
	public void solveHeptadoku1() throws IOException {
		addTxtSolve("heptadoku1");
	}
	
	@Test
	public void solveOctadoku1() throws IOException {
		addTxtSolve("octadoku1");
	}
	
	@Test
	public void solveEnneadoku1() throws IOException {
		addTxtSolve("enneadoku1");
	}
	
//	@Test
	public void solveDecadoku1() throws IOException {
		addTxtSolve("decadoku1");
	}
	
	@Test
	public void top95() throws IOException {
		FileReader f = new FileReader("puzzles/top95.txt");
		BufferedReader b = new BufferedReader(f);
		
		TacticSolver[] solvers = new TacticSolver[95];
		
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

			solvers[n] = new TacticSolver(new Grid(sb.toString()));
			n++;
		}

		b.close();
		
		for (int i = 0; i < solvers.length; i++) {
			assertTrue(solvers[i].solve() != null);
		}
	}
	
	@Test
	public void exampleIsSolvable() throws IOException {
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
		TacticSolver solver = new TacticSolver(grid);
		
		assertTrue(solver.solvable());
	}
	
	@Test
	public void uniqueness() throws FileNotFoundException, IOException {
		Grid[] grids = { new Grid(new FileReader("puzzles/sudoku_norvig_impossible.txt")),
				new Grid(new FileReader("puzzles/sudoku_norvig_hardest.txt")),
				null,
				new Grid(new FileReader("puzzles/pentadoku1.txt"))
		};
		
		TacticSolver s = new TacticSolver(grids[0]);
		assertFalse(s.unique());
		
		s = new TacticSolver(grids[1]);
		assertFalse(s.unique());
		
		s = new TacticSolver(grids[2]);
		assertFalse(s.unique());
		
		s = new TacticSolver(grids[3]);
		assertTrue(s.unique());
	}
	
	@Test
	public void solvability() throws IOException {
		String input =
				"3\n" +
				"1;.;.;.;.;.;.;.;." + "\n" +
				".;2;.;.;.;.;.;.;." + "\n" +
				".;.;3;.;.;.;.;.;." + "\n" +
				"4;.;.;.;.;.;.;.;." + "\n" +
				"5;.;.;.;.;.;.;.;." + "\n" +
				"6;.;.;.;.;.;.;.;." + "\n" +
				"7;.;.;.;.;.;.;.;." + "\n" +
				"8;.;.;.;.;.;.;.;." + "\n" +
				"9;.;.;.;.;.;.;.;." ;
		
		
		Grid g = new Grid(input);
		Solver s = new TacticSolver(g);
		assertEquals(g, s.getGrid());
		
		assertFalse(s.solvable());
		
		s = new TacticSolver(null);
		assertFalse(s.solvable());
		
		s = new TacticSolver(new Grid(4));
		assertTrue(s.solvable());
	}
	
	@Test
	public void timeouts() throws FileNotFoundException, IOException {
		Grid[] grids = { new Grid(new FileReader("puzzles/sudoku_norvig_impossible.txt")),
				new Grid(new FileReader("puzzles/sudoku_norvig_hardest.txt")),
				new Grid(new FileReader("puzzles/pentadoku1.txt"))
		};
		
		Grid g = grids[0];
		TacticSolver s = new TacticSolver(g);
		int t = 1000;
		
		assertFalse(g.isSolved());

		assertNull(s.solveWithTimeout(t));
		assertFalse(s.uniqueWithTimeout(t));
		assertFalse(s.solvableWithTimeout(t));
		
		g = grids[1];
		s = new TacticSolver(g);
		
		assertFalse(g.isSolved());

		assertTrue(s.solveWithTimeout(t).isSolved());
		assertFalse(s.uniqueWithTimeout(t));
		assertTrue(s.solvableWithTimeout(t));
		
		g = grids[2];
		s = new TacticSolver(g);
		
		assertFalse(g.isSolved());

		assertTrue(s.solveWithTimeout(t).isSolved());
		assertTrue(s.uniqueWithTimeout(t));
		assertTrue(s.solvableWithTimeout(t));
		
		t = 1;
		
		assertNull(s.solveWithTimeout(t));
		assertFalse(s.uniqueWithTimeout(t));
		assertFalse(s.solvableWithTimeout(t));
	}
	
}
