package model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Grid;
import model.PuzzleGenerator;
import model.Solver;

public class PuzzleGeneratorTest {

	public void generalTest(int k) {
		Grid g = PuzzleGenerator.generate(k);
		
		Solver s = new Solver(g);
		
		assertFalse(g.isSolved());
		assertTrue(s.unique());
		
		g = s.solve();
		
		assertTrue(g.isSolved());
		assertTrue(s.unique());
	}
	
	@Test
	public void generate2() {
		for (int i = 0; i < 100; i++) {
			generalTest(2);
		}
	}
	
	@Test
	public void generate3() {
		for (int i = 0; i < 100; i++) {
			generalTest(3);
		}
	}
	
//	@Test
	public void generate4() {
		for (int i = 0; i < 1; i++) {
			generalTest(4);
		}
	}
	
//	@Test
	public void generate5() {
		for (int i = 0; i < 1; i++) {
			generalTest(5);
		}
	}
}
