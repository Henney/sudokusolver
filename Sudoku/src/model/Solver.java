package model;

public abstract class Solver {
	
	protected Grid grid;
	
	protected volatile boolean run = true;
	
	public Solver(Grid g) {
		this.grid = g;
	}

	public abstract Grid solve();
	
	public void cancel() {
		run = false;
	}

	public boolean solvable() {
		if (!grid.isLegal()) {
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
	
	public Grid getGrid() {
		return grid;
	}
}
