package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class BoxTactic extends Tactic {

	public BoxTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public boolean apply(int field, int value) {
		return pGrid.setBoxImpossible(field, value);
	}

}
