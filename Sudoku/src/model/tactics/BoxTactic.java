package model.tactics;

import model.Grid;
import model.PossibleValuesGrid;

public class BoxTactic extends Tactic {

	public BoxTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
	}
	
	@Override
	public void apply(int field, int value) {
		pGrid.setBoxImpossible(field, value);
	}

}
