package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class RowTactic extends Tactic {

	public RowTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public void apply(int field, int value) {
		pGrid.setRowImpossible(field, value);
	}

}
