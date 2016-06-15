package model.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import model.SATSolver;
import model.TacticSolver;
import model.Grid;
import model.SATSolver;

public class SATTest {

	@Test
	public void solve2() throws IOException {
		String input = "2\n" + "1;2;.;.\n" + ".;.;.;.\n" + ".;3;.;.\n" + ".;.;.;4\n";

		Grid grid = new Grid(input);

		SATSolver solver = new SATSolver(grid);
		Grid solved = solver.solve();

		assertNotNull(solved);
		assertTrue(solved.isSolved());
	}

	@Test
	public void solveExample() throws IOException {
		String input = "3\n" + ".;1;.;3;.;.;8;.;." + "\n" + "5;.;9;6;.;.;7;.;." + "\n" + "7;.;4;.;9;5;.;2;." + "\n"
				+ "4;.;.;.;.;.;1;.;." + "\n" + ".;2;8;.;7;1;.;6;3" + "\n" + ".;.;.;2;.;4;9;5;." + "\n"
				+ "6;.;3;.;.;9;.;.;7" + "\n" + ".;.;.;4;2;.;5;1;6" + "\n" + ".;5;2;.;8;.;.;4;.";

		Grid grid = new Grid(input);

		SATSolver solver = new SATSolver(grid);
		Grid solved = solver.solve();

		assertTrue(solved != null);
		assertTrue(solved.isSolved());
	}

	@Test
	public void solveTetradoku1() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/tetradoku1.txt"));

		Grid solved = new SATSolver(g).solve();

		assertTrue(solved != null);
		assertTrue(solved.isSolved());
	}

	// @Test
	public void solveHeptadoku() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/heptadoku1.txt"));
		assertFalse(g.isSolved());
		Grid solved = new SATSolver(g).solve();
		assertNotNull(solved);
		assertTrue(solved.isSolved());
	}

	// @Test
	public void solveOctadoku() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/octadoku1.txt"));
		assertFalse(g.isSolved());
		Grid solved = new SATSolver(g).solve();
		assertNotNull(solved);
		assertTrue(solved.isSolved());
	}

	// @Test
	public void solveEnneadoku() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/enneadoku1.txt"));
		assertFalse(g.isSolved());
		Grid solved = new SATSolver(g).solve();
		assertNotNull(solved);
		assertTrue(solved.isSolved());
	}

	// @Test
	public void solveDecadoku() throws IOException {
		Grid g = new Grid(new FileReader("puzzles/decadoku1.txt"));
		assertFalse(g.isSolved());
		Grid solved = new SATSolver(g).solve();
		assertNotNull(solved);
		assertTrue(solved.isSolved());
	}

	@Test
	public void solveTop95() throws IOException {
		FileReader f = new FileReader("puzzles/top95.txt");
		BufferedReader b = new BufferedReader(f);

		Grid[] grids = new Grid[95];

		int n = 0;

		while (b.ready()) {
			String line = b.readLine();

			StringBuilder sb = new StringBuilder();
			sb.append("3\n");

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 8; j++) {
					sb.append(line.charAt(i * 9 + j));
					sb.append(";");
				}
				sb.append(line.charAt(i * 9 + 8));

				sb.append('\n');
			}

			grids[n] = new Grid(sb.toString());
			n++;
		}

		b.close();

		for (int i = 0; i < grids.length; i++) {
			Grid solved = new SATSolver(grids[i]).solve();
			assertTrue(solved != null);
			assertTrue(solved.isSolved());
		}
	}

	@Test
	public void solvability() throws IOException {
		String input = "3\n" + "1;.;.;.;.;.;.;.;." + "\n" + ".;2;.;.;.;.;.;.;." + "\n" + ".;.;3;.;.;.;.;.;." + "\n"
				+ "4;.;.;.;.;.;.;.;." + "\n" + "5;.;.;.;.;.;.;.;." + "\n" + "6;.;.;.;.;.;.;.;." + "\n"
				+ "7;.;.;.;.;.;.;.;." + "\n" + "8;.;.;.;.;.;.;.;." + "\n" + "9;.;.;.;.;.;.;.;.";

		Grid g = new Grid(input);
		SATSolver s = new SATSolver(g);

		assertFalse(s.solvable());

		s = new SATSolver(null);
		assertFalse(s.solvable());

		s = new SATSolver(new Grid(4));
		assertTrue(s.solvable());
	}

	@Test
	public void timeouts() throws FileNotFoundException, IOException {
		Grid[] grids = { new Grid(new FileReader("puzzles/sudoku_norvig_impossible.txt")),
				new Grid(new FileReader("puzzles/sudoku_norvig_hardest.txt")),
				new Grid(new FileReader("puzzles/tetradoku1.txt")) };

		Grid g = grids[0];
		SATSolver s = new SATSolver(g);
		int t = 2000;

		assertFalse(g.isSolved());

		assertNull(s.solveWithTimeout(t));
		assertFalse(s.solvableWithTimeout(t));

		g = grids[1];
		s = new SATSolver(g);

		assertFalse(g.isSolved());
		assertTrue(s.solveWithTimeout(t).isSolved());
		assertTrue(s.solvableWithTimeout(t));

		g = grids[2];
		s = new SATSolver(g);

		assertFalse(g.isSolved());

		assertTrue(s.solveWithTimeout(t).isSolved());
		assertTrue(s.solvableWithTimeout(t));

		t = 1;

		assertNull(s.solveWithTimeout(t));
		assertFalse(s.solvableWithTimeout(t));
	}

	@Test
	public void uniqueness() throws FileNotFoundException, IOException {
		Grid[] grids = { new Grid(new FileReader("puzzles/sudoku_norvig_impossible.txt")),
				new Grid(new FileReader("puzzles/sudoku_norvig_hardest.txt")), null,
				new Grid(new FileReader("puzzles/pentadoku1.txt")) };

		SATSolver s = new SATSolver(grids[0]);
		assertFalse(s.unique());

		s = new SATSolver(grids[1]);
		assertFalse(s.unique());

		s = new SATSolver(grids[2]);
		assertFalse(s.unique());

		s = new SATSolver(grids[3]);
		assertTrue(s.unique());
	}
}
