package model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Grid;
import model.PuzzleGenerator;
import model.TacticSolver;

public class PuzzleGeneratorTest {

	public void generalTest(int k, long seed) { // TODO remove seed
		Grid g = PuzzleGenerator.generate(k, seed);
		
		TacticSolver s = new TacticSolver(g);
		
		assertFalse(g.isSolved());
		assertTrue(s.unique());
		
		g = s.solve();

		assertTrue(g.isSolved());
		assertTrue(s.unique());
	}
	
//	@Test
//	public void generate2() {
//		for (int i = 0; i < 100; i++) {
//			generalTest(2);
//		}
//	}
//	
//	@Test
//	public void generate3() {
//		for (int i = 0; i < 10; i++) {
//			generalTest(3);
//		}
//	}
	
	@Test
	public void generate4() {
		for (int i = 0; i < 1; i++) {
			generalTest(4, i+100);
		}
	}
	
//	@Test
//	public void generate5() {
//		for (int i = 0; i < 1; i++) {
//			generalTest(5, i+100);
//		}
//	}
}
