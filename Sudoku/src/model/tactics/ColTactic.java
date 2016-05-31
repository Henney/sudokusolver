package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class ColTactic extends Tactic {

	public ColTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public boolean apply(int field, int value) {
		return pGrid.setColImpossible(field, value);
	}

}
