package model;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;


public class UserGrid {
	
	private Grid grid;
	
	private ArrayDeque<Integer>[][] rows;
	private ArrayDeque<Integer>[][] cols;
	private ArrayDeque<Integer>[][] boxes;

	@SuppressWarnings("unchecked")
	private void initPossibleValues() {
		rows = new ArrayDeque[grid.size()][grid.size()];
		cols = new ArrayDeque[grid.size()][grid.size()];
		boxes = new ArrayDeque[grid.size()][grid.size()];
		
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[i].length; j++) {
				rows[i][j] = new ArrayDeque<Integer>();
				cols[i][j] = new ArrayDeque<Integer>();
				boxes[i][j] = new ArrayDeque<Integer>();
			}
		}
		
		for (int i = 0; i < grid.size(); i++) {
			int x = grid.get(i);
			
			rows[grid.rowFor(i)][x].add(i);
			cols[grid.colFor(i)][x].add(i);
			boxes[grid.boxFor(i)][x].add(i);
		}
	}
	
	public UserGrid(int k) {
		grid = new Grid(k);
		initPossibleValues();
	}

	public UserGrid(String input) throws IOException {
		grid = new Grid(input);
		initPossibleValues();
	}
	
	public UserGrid(Reader input) throws IOException {
		grid = new Grid(input);
		initPossibleValues();
	}
	
	public int get(int row, int col) {
		return grid.get(row, col);
	}
	
	public int get(int i) {
		return grid.get(i);
	}
	
	public Set<Integer> set(int row, int col, int val) {
		return set(row*grid.size() + col, val);
	}
	
	public Set<Integer> set(int i, int val) {
		grid.set(i, val);
		
		HashSet<Integer> conflicting = new HashSet<Integer>();
		
		final int row = grid.rowFor(i);
		final int col = grid.colFor(i);
		final int box = grid.boxFor(i);
		
		int old = grid.get(i);
		
		if (old != 0) {
			rows[row][old].remove(i);
			cols[col][old].remove(i);
			boxes[box][old].remove(i);
		}
		
		ArrayDeque<Integer> r = rows[row][val];
		r.push(i);
		if (r.size() > 1) {
			conflicting.addAll(r);
		}

		ArrayDeque<Integer> c = cols[col][val];
		c.push(i);
		if (c.size() > 1) {
			conflicting.addAll(c);
		}
		
		ArrayDeque<Integer> b = boxes[box][val];
		b.push(i);
		if (b.size() > 1) {
			conflicting.addAll(b);
		}
				
		return conflicting;
	}
}
