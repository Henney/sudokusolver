package model;

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
		this.timeout = t;
		this.start = System.currentTimeMillis();
		boolean b = solvable();
		this.timeout = 0;
		return b;
	}
	
	public Grid getGrid() {
		return grid;
	}
}
