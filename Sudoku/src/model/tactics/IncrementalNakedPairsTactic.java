package model.tactics;

import java.util.ArrayDeque;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.util.Node;

public class IncrementalNakedPairsTactic extends AlwaysTactic {

	private PossibleValues[] pvs;

	private ArrayDeque<Integer> rows;
	private ArrayDeque<Integer> cols;
	private ArrayDeque<Integer> boxs;

	public IncrementalNakedPairsTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);

		pvs = pGrid.getPossibleValues();

		rows = new ArrayDeque<Integer>();
		cols = new ArrayDeque<Integer>();
		boxs = new ArrayDeque<Integer>();
	}

	@Override
	public void apply(int field, int value) {
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
		
		iterate(rows);
		iterate(cols);
		iterate(boxs);
	}
	
	private void iterate(ArrayDeque<Integer> bucket) {
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
		
					if (grid.rowFor(f1) == grid.rowFor(f2)) {
						pGrid.setRowImpossible(f1, x);
						pGrid.setRowImpossible(f1, y);
					}

					if (grid.colFor(f1) == grid.colFor(f2)) {
						pGrid.setColImpossible(f1, x);
						pGrid.setColImpossible(f1, y);
					}
					
					if (grid.boxFor(f1) == grid.boxFor(f2)) {
						pGrid.setBoxImpossible(f1, x);
						pGrid.setBoxImpossible(f1, y);
					}
					
					pvs[f1] = p1;
					pvs[f2] = p2;
				}
			}
		}
	}

}
