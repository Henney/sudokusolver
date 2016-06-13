package model.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import model.Grid;
import model.UserGrid;
import model.util.Pair;

public class UserGridTest {

	@Test
	public void setPossible() {
		UserGrid grid = new UserGrid(new Grid(3));

		assertTrue(grid.set(0, 1).fst.isEmpty());
		assertTrue(grid.set(1, 2).fst.isEmpty());
		
		Pair<Set<Integer>, Set<Integer>> s21 = grid.set(2, 1);
		
		assertTrue(s21.fst.contains(0));
		assertTrue(s21.fst.contains(2));
		
		assertTrue(s21.snd.isEmpty());
		
		Pair<Set<Integer>, Set<Integer>> s23 = grid.set(2, 3);
		
		assertTrue(s23.fst.isEmpty());
		assertTrue(s23.snd.contains(0));
		
		grid.set(9, 9);
		Pair<Set<Integer>, Set<Integer>> s00 = grid.set(0, 0);
		
		assertTrue(s00.fst.isEmpty());
		assertTrue(s00.snd.isEmpty());
		
		Pair<Set<Integer>, Set<Integer>> s09 = grid.set(0, 9);
		
		assertTrue(s09.fst.contains(0));
		assertTrue(s09.fst.contains(9));
		assertTrue(s09.snd.isEmpty());

		Pair<Set<Integer>, Set<Integer>> s802 = grid.set(80, 2);
		assertTrue(s802.fst.isEmpty());
		assertTrue(s802.snd.isEmpty());
	}
	
	@Test
	public void setPossibleRevert() {
		UserGrid grid = new UserGrid(3);

		assertTrue(grid.set(1, 1, 1).fst.isEmpty());
		
		assertEquals(2, grid.set(0, 1).fst.size());
		assertEquals(2, grid.set(16, 1).fst.size());
		assertEquals(2, grid.set(28, 1).fst.size());

		Pair<Set<Integer>, Set<Integer>> cr = grid.set(1, 1, 2);
		assertEquals(0, cr.fst.size());
		assertEquals(3, cr.snd.size());
		
		cr.toString();
		
	}
	
	@Test
	public void box9regression() throws IOException {
		UserGrid grid = new UserGrid(new FileReader("puzzles/sudoku1.txt"));
		
		int other = 3*9;
		assertTrue(grid.get(other) == 9);
		
		int i = 3*9 + 4;
		assertTrue(grid.rowFor(other) == grid.rowFor(i));
		
		Pair<Set<Integer>, Set<Integer>> s34 = grid.set(i, 9);

		assertTrue(s34.fst.contains(other));
	}
	
	@Test
	public void keepConflicts() throws IOException {
		String input = "3\n" +
				".;.;.;9;6;.;.;.;3\n" +
				"6;.;8;.;.;.;.;.;.\n" +
				"3;4;1;.;.;8;.;.;5\n" +
				"9;2;.;4;.;.;3;.;1\n" +
				".;7;.;1;5;6;.;2;.\n" +
				"4;.;6;.;.;2;.;8;7\n" +
				"2;.;.;7;.;.;4;3;8\n" +
				".;.;.;.;.;.;7;.;6\n" +
				"1;.;.;.;4;3;.;.;.";
				
		UserGrid grid = new UserGrid(input);

		// Set field 44 (row 4, col 8 - 0 indexed) to 7, resulting in conflicts with 37 and 53
		Pair<Set<Integer>, Set<Integer>> s447 = grid.set(44, 7);
		assertTrue(s447.fst.contains(37));
		assertTrue(s447.fst.contains(44));
		assertTrue(s447.fst.contains(53));
		assertTrue(s447.snd.isEmpty());

		Pair<Set<Integer>, Set<Integer>> s440 = grid.set(44, 0);
		assertTrue(s440.fst.isEmpty());
		assertTrue(s440.snd.contains(37));
		assertTrue(s440.snd.contains(53));
		grid.set(44, 7);

		Pair<Set<Integer>, Set<Integer>> s373 = grid.set(37, 3);
		assertTrue(s373.fst.isEmpty());
		assertTrue(s373.snd.isEmpty());
		grid.set(37, 7);

		Pair<Set<Integer>, Set<Integer>> s531 = grid.set(53, 0);
		assertTrue(s531.fst.isEmpty());
		assertTrue(s531.snd.isEmpty());
	}
}
