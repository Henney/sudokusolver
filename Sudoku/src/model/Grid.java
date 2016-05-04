package model;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import model.util.Pair;

public class Grid {

	private int[] grid;
	private int k;
	private int n;
	
	public Grid(Grid other) {
		this.grid = Arrays.copyOf(other.grid, other.grid.length);
		this.k = other.k;
		this.n = other.n;
	}
	
	public Grid(String input) throws IOException {
		this(new StringReader(input));
	}
	
	public Grid(Reader input) throws IOException {
		Pair<int[], Integer> parsed = Parser.parseGrid(input);
		this.grid = parsed.fst;
		this.k = parsed.snd;
		this.n = k*k;
	}
	
	public int numberOfFields() {
		return n*n;
	}
	
	public int size() {
		return n;
	}
	
	public int k() {
		return k;
	}
	
	public int get(int i) {
		return grid[i];
	}
	
	public void set(int i, int val) {
		grid[i] = val;
	}
	
	public int get(int row, int col) {
		return this.grid[n*row + col];
	}

	public void set(int row, int col, int val) {
		this.grid[n*row + col] = val;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int row = 0; row < size(); row++) {
			for (int col = 0; col < size(); col++) {
				int x = get(row, col);
				
				if (x == 0) {
					sb.append("  ");
				} else {
					sb.append(get(row, col) + " ");
				}
				
				if (col+1 < size() && (col+1) % k == 0) {
					sb.append("| ");
				}
			}
			
			sb.append(System.lineSeparator());
			

			if (row+1 < size() && (row+1) % k == 0) {
				// TODO: probably not entirely correct ;)
				for (int col = 0; col < size()+k-1; col++) {
					sb.append("- ");
				}
				
				sb.append(System.lineSeparator());
			}
		}
		
		return sb.toString();
	}
	
}
