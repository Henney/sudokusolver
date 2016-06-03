package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public abstract class AlwaysTactic extends Tactic {
	
	public AlwaysTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}

	public abstract void apply(int field, int value);

}
