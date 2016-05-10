package model.tests;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import model.UserGrid;

public class UserGridTest {

	@Test
	public void setPossible() {
		UserGrid grid = new UserGrid(3);

		assertTrue(grid.set(0, 1).fst.isEmpty());
		assertTrue(grid.set(1, 2).fst.isEmpty());
		
		Set<Integer> conflicting = grid.set(2, 1).fst;
		System.out.println(conflicting);
		assertTrue(conflicting.contains(0));
	}

}
