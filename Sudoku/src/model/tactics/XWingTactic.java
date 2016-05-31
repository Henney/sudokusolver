package model.tactics;

import java.util.ArrayDeque;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.util.Pair;
import model.util.XWingBucket;

public class XWingTactic extends ChoiceTactic {
	

	PossibleValues[] pvs;

	XWingBucket buck;

	public XWingTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
		
		pvs = pGrid.getPossibleValues();

		buck = new XWingBucket(n);
	}

	@Override
	public void apply() throws UnsolvableException {
		for (int val = 1; val <= n; val++) {
			xWingRow(val);
			xWingCol(val);
		}
	}

	// TODO remove redundancy of these two methods if possible
	private void xWingRow(int val) {
		for (int rowIdx = 0; rowIdx < n*n; rowIdx += n) {
			ArrayDeque<Integer> fields = new ArrayDeque<Integer>();
			for (int f = rowIdx; f < rowIdx + n; f++) {
				if (pvs[f] != null && pvs[f].get(val)) {
					fields.add(f);
				}
			}
			
			if (fields.size() == 2) {
				int f1 = fields.poll();
				int f2 = fields.poll();
				buck.add(grid.colFor(f1), grid.colFor(f2), f1, f2);
			}
		}
		
		for (Pair<Pair<Integer, Integer>,
				  Pair<Integer, Integer>> x : buck.getBest()) {
			int field1 = x.fst.fst;
			int field2 = x.fst.snd;
			int field3 = x.snd.fst;
			int field4 = x.snd.snd;

			int col1 = grid.colFor(field1);
			int col2 = grid.colFor(field2);
							
			for (int field = col1; field < n*n; field += n) {
				if (field == field1 || field == field3) continue;
				pGrid.updateField(field, val);
			}
			
			for (int field = col2; field < n*n; field += n) {
				if (field == field2 || field == field4) continue;
				pGrid.updateField(field, val);
			}
		}
		buck.clear();
	}

	private void xWingCol(int val) {
		for (int colIdx = 0; colIdx < n; colIdx++) {
			ArrayDeque<Integer> fields = new ArrayDeque<Integer>();
			for (int f = colIdx; f < n*n; f += n) {
				if (pvs[f] != null && pvs[f].get(val)) {
					fields.add(f);
				}
			}
			
			if (fields.size() == 2) {
				int f1 = fields.poll();
				int f2 = fields.poll();
				buck.add(grid.rowFor(f1), grid.rowFor(f2), f1, f2);
			}
		}
		
		for (Pair<Pair<Integer, Integer>,
				  Pair<Integer, Integer>> x : buck.getBest()) {
			int field1 = x.fst.fst;
			int field2 = x.fst.snd;
			int field3 = x.snd.fst;
			int field4 = x.snd.snd;
			int row1 = grid.rowFor(field1);
			int row2 = grid.rowFor(field2);
							
			for (int field = row1*n; field < row1 + n; field++) {
				if (field == field1 || field == field3) continue;
				pGrid.updateField(field, val);
			}
			
			for (int field = row2*n; field < row1 + n; field++) {
				if (field == field2 || field == field4) continue;
				pGrid.updateField(field, val);
			}
		}
		buck.clear();
	}

}
