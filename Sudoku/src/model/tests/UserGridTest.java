package model.tests;

import static org.junit.Assert.assertTrue;

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

		System.out.println(s23.snd);
		
		assertTrue(s23.fst.isEmpty());
		assertTrue(s23.snd.contains(0));
	}

}
