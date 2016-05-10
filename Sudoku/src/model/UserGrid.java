package model;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import model.util.Pair;


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
			
			if (x != 0) {
				x--;
				rows[grid.rowFor(i)][x].add(i);
				cols[grid.colFor(i)][x].add(i);
				boxes[grid.boxFor(i)][x].add(i);
			}
		}
	}
	
	public UserGrid(Grid grid) {
		this.grid = grid;
		initPossibleValues();
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
	
	public Pair<Set<Integer>, Set<Integer>> set(int row, int col, int val) {
		return set(row*grid.size() + col, val);
	}
	
	public Pair<Set<Integer>, Set<Integer>> set(int i, int val) {
		int old = grid.get(i);
		grid.set(i, val);
		
		HashSet<Integer> conflicting = new HashSet<Integer>();
		HashSet<Integer> resolved = new HashSet<Integer>();
		
		final int row = grid.rowFor(i);
		final int col = grid.colFor(i);
		final int box = grid.boxFor(i);
		
		if (old != 0) {
			System.out.println(i + ", " + val);
			
			old--;
			
			ArrayDeque<Integer> r = rows[row][old];
			ArrayDeque<Integer> c = cols[col][old];
			ArrayDeque<Integer> b = boxes[box][old];
			
			r.remove(i);
			c.remove(i);
			b.remove(i);
			
			if (r.size() == 1) {
				int f = r.peek();
				
				if(!inConflict(f)) {
					resolved.add(f);
				}
			}
			
			if (c.size() == 1) {
				int f = c.peek();
				
				if(!inConflict(f)) {
					resolved.add(f);
				}
			}
			
			if (b.size() == 1) {
				int f = b.peek();
				
				if(!inConflict(f)) {
					resolved.add(f);
				}
			}
		}
		
		System.out.println(i + ", " + val + ": " + resolved);
		
		if (val == 0) {
			return new Pair<Set<Integer>, Set<Integer>>(conflicting, resolved);
		}
		
		ArrayDeque<Integer> r = rows[row][val-1];
		r.push(i);
		if (r.size() > 1) {
			conflicting.addAll(r);
		}

		ArrayDeque<Integer> c = cols[col][val-1];
		c.push(i);
		if (c.size() > 1) {
			conflicting.addAll(c);
		}
		
		ArrayDeque<Integer> b = boxes[box][val-1];
		b.push(i);
		if (b.size() > 1) {
			conflicting.addAll(b);
		}
			
		return new Pair<Set<Integer>, Set<Integer>>(conflicting, resolved);
	}
	
	private boolean inConflict(int i) {
		final int row = grid.rowFor(i);
		final int col = grid.colFor(i);
		final int box = grid.boxFor(i);
		
		int x = grid.get(i);
		
		if (x == 0) {
			return false;
		}
		
		x--;
		
		return rows[row][x].size() > 1 || cols[col][x].size() > 1 || boxes[box][x].size() > 1;

	}
	
	public int k() {
		return grid.k();
	}
	
	public int size() {
		return grid.size();
	}
	
	public Grid getGrid() {
		return grid;
	}
}
