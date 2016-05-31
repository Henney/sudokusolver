package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public abstract class ChoiceTactic extends Tactic {
	
	public ChoiceTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}

	public abstract void apply() throws UnsolvableException;

}
