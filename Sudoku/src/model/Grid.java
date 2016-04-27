package model;

import java.io.IOException;
import java.io.Reader;

import model.util.Pair;

public class Grid {

	private int[] grid;
	private int k;
	private int n;
	
	public Grid(Reader input) throws IOException {
		Pair<int[], Integer> parsed = Parser.parseGrid(input);
		this.grid = parsed.fst;
		this.k = parsed.snd;
		this.n = k*k;
	}
	
	public int get(int row, int col) {
		return this.grid[n*row + col];
	}
}
