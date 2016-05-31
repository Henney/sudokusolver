package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public abstract class Tactic {

	protected Grid grid;
	protected int n;
	protected int k;
	protected PossibleValuesGrid pGrid;
	
	public Tactic(Grid grid, PossibleValuesGrid pGrid) {
		this.grid = grid;
		this.n = grid.size();
		this.k = grid.k();
		this.pGrid = pGrid;
	}
	
	public abstract void apply(int field, int value) throws UnsolvableException;
	
}
