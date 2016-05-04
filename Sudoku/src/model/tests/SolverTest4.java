package model.tests;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import model.Grid;
import model.Solver;

public class SolverTest4 {
	
	public void solveSudoku(String filename) throws IOException {
		Grid g = new Grid(new FileReader("4puzzles/" + filename + ".txt"));
		
		assertFalse(g.isSolved());
		
		System.out.println(g);
		
		Solver solver = new Solver(g);
		Grid solved = solver.solve();
		
//		if (!solved.isSolved()) {
			System.out.println(filename);
			System.out.println(solved);
//		}
		
		assertTrue(solved.isSolved());
	}
	
	@Test
	public void solveHexadoku1() throws IOException {
		solveSudoku("hexadoku1");
	}
	
	@Test
	public void solveHexadoku2() throws IOException {
		solveSudoku("hexadoku2");
	}
	
}
