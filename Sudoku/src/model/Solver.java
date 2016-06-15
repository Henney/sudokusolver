package model;

import java.io.IOException;

public abstract class Solver {
	
	protected Grid grid;

	protected int timeout = 0;
	protected long start;
	
	protected volatile boolean run = true;
	
	public Solver(Grid g) {
		this.grid = g;
	}

	public abstract Grid solve();
	
	public Grid solveWithTimeout(int t) {
		start = System.currentTimeMillis();
		timeout = t;	
		Grid g = solve();
		timeout = 0;
		return g;
	}
	
	public void cancel() {
		run = false;
	}

	public boolean solvable() {
		if (grid == null || !grid.isLegal()) {
			return false;
		}
		
		PossibleValues[] ps = grid.findPossibleValues();
		
		for (int i = 0; i < ps.length; i++) {
			if (grid.get(i) == 0 && ps[i].possible() == 0) {
				return false;
			}
		}
		
		return solve() != null;
	}
	
	public boolean solvableWithTimeout(int t) {
		timeout = t;
		start = System.currentTimeMillis();
		boolean b = solvable();
		timeout = 0;
		return b;
	}
	
	public abstract boolean unique();
	
	public boolean uniqueWithTimeout(int t) {
		timeout = t;
		boolean ret = unique();
		timeout = 0;
		return ret;
	}
	
	public Grid getGrid() {
		return grid;
	}
}
