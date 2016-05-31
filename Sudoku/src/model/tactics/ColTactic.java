package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class ColTactic extends AlwaysTactic {

	public ColTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public void apply(int field, int value) {
		pGrid.setColImpossible(field, value);
	}

}
