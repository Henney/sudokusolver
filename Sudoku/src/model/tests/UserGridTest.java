package model.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import model.UserGrid;
import model.util.Pair;

public class UserGridTest {

	@Test
	public void setPossible() {
		UserGrid grid = new UserGrid(3);

		assertTrue(grid.set(0, 1).fst.isEmpty());
		assertTrue(grid.set(1, 2).fst.isEmpty());
		
		Pair<Set<Integer>, Set<Integer>> s21 = grid.set(2, 1);
		
		assertTrue(s21.fst.contains(0));
		assertTrue(s21.fst.contains(2));
		
		assertTrue(s21.snd.isEmpty());
		
		Pair<Set<Integer>, Set<Integer>> s23 = grid.set(2, 3);
		
		assertTrue(s23.fst.isEmpty());
		assertTrue(s23.snd.contains(0));
	}
	
	@Test
	public void box9regression() throws IOException {
		UserGrid grid = new UserGrid(new FileReader("puzzles/sudoku1.txt"));
		
		int other = 3*9;
		assertTrue(grid.get(other) == 9);
		
		int i = 3*9 + 4;
		assertTrue(grid.getGrid().rowFor(other) == grid.getGrid().rowFor(i));
		
		Pair<Set<Integer>, Set<Integer>> s34 = grid.set(i, 9);
		
		System.out.println(s34.fst);
		
		assertTrue(s34.fst.contains(other));
	}

}