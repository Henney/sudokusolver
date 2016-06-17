package model;

public abstract class Solver {
	
	protected Grid grid;

	protected int timeout = 0;
	protected long start = 0;
	
	protected volatile boolean run = true;
	
	public Solver(Grid g) {
		this.grid = g;
	}

	public abstract Grid solve();
	
	public Grid solveWithTimeout(int t) {
		startTimeout(t);
		
		Grid g;
		try {
			g = solve();
		} finally {
			endTimeout();
		}
		
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

		Grid solved = solve();
		return solved != null;
	}
	
	public boolean solvableWithTimeout(int t) {
		startTimeout(t);
		boolean b;
		try {
			b = solvable();
		} finally {
			endTimeout();
		}
		return b;
	}
	
	public abstract boolean unique();
	
	public boolean uniqueWithTimeout(int t) {
		startTimeout(t);
		boolean ret;
		try {
			ret = unique();
		} finally {
			endTimeout();
		}
		return ret;
	}
	
	private void startTimeout(int t) {
		start = System.currentTimeMillis();
		timeout = t;
	}
	
	private void endTimeout() {
		start = 0;
		timeout = 0;
	}
	
	public Grid getGrid() {
		return grid;
	}
}
