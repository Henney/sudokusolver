package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class RowTactic extends Tactic {

	public RowTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public boolean apply(int field, int value) {
		return pGrid.setRowImpossible(field, value);
	}

}
