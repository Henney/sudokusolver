package model.tactics;

import java.util.ArrayDeque;
import java.util.function.BiFunction;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.util.Node;

public class IncrementalTwinsTactic extends Tactic {

	private PossibleValues[] pvs;

	private ArrayDeque<Integer> rows;
	private ArrayDeque<Integer> cols;
	private ArrayDeque<Integer> boxs;

	public IncrementalTwinsTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);

		pvs = pGrid.getPossibleValues();

		rows = new ArrayDeque<Integer>();
		cols = new ArrayDeque<Integer>();
		boxs = new ArrayDeque<Integer>();
	}

	@Override
	public void apply(int field, int value) throws UnsolvableException {
		final int row = grid.rowFor(field);
		final int col = grid.colFor(field);
		final int box = grid.boxFor(field);
			
		rows.clear();
		cols.clear();
		boxs.clear();

		Node head = pGrid.valuesWithPrio(2).peek();

		while (head != null) {
			int i = head.getValue();
			
			if (grid.rowFor(i) == row) {
				rows.add(i);
			}
			
			if (grid.colFor(i) == col) {
				cols.add(i);
			}
			
			if (grid.boxFor(i) == box) {
				boxs.add(i);
			}

			head = head.getNext();
		}
		
		iterate(rows, (f, x) -> { pGrid.setRowImpossible(f, x); return null; });
		iterate(cols, (f, x) -> { pGrid.setColImpossible(f, x); return null; });
		iterate(boxs, (f, x) -> { pGrid.setBoxImpossible(f, x); return null; });
	}
	
	private void iterate(ArrayDeque<Integer> bucket, BiFunction<Integer, Integer, Void> setImpossible) {
		while (!bucket.isEmpty()) {
			int f1 = bucket.pop();
			PossibleValues p1 = pvs[f1];
			
			int x = p1.nextAfter(0);
			int y = p1.nextAfter(x);
			
			for (int f2 : bucket) {
				PossibleValues p2 = pvs[f2];
				
				if (p1.equals(p2)) {
					
					pvs[f1] = null;
					pvs[f2] = null;
		
					setImpossible.apply(f1, x);
					setImpossible.apply(f1, y);
					
					pvs[f1] = p1;
					pvs[f2] = p2;
				}
			}
		}
	}

}
