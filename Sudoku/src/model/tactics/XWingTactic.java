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
		xWing(true);
		xWing(false);
	}

	private void xWing(boolean isRow) {
		// isRow is true if we are doing X-Wing on rows, false if it is on columns
		// It controls flow throughout the method in a not necessarily very intuitive way
		for (int val = 1; val <= n; val++) {
			for (int idx = 0; idx < lim1(isRow); idx += inc(isRow)) {
				ArrayDeque<Integer> fields = new ArrayDeque<Integer>();
				for (int f = idx; f < lim2(isRow, idx); f += inc(!isRow)) {
					if (pvs[f] != null && pvs[f].get(val)) {
						fields.add(f);
					}
				}
				
				if (fields.size() == 2) {
					int f1 = fields.poll();
					int f2 = fields.poll();
					int idx1 = isRow ? grid.colFor(f1) : grid.rowFor(f1);
					int idx2 = isRow ? grid.colFor(f2) : grid.rowFor(f2);
					buck.add(idx1, idx2, f1, f2);
				}
			}
			
			for (Pair<Pair<Integer, Integer>,
					  Pair<Integer, Integer>> x : buck.getBest()) {
				int f1 = x.fst.fst;
				int f2 = x.fst.snd;
				int f3 = x.snd.fst;
				int f4 = x.snd.snd;
				int idx1 = isRow ? grid.colFor(f1) : grid.rowFor(f1);
				int idx2 = isRow ? grid.colFor(f2) : grid.rowFor(f2);
								
				for (int field = idx1*inc(!isRow); field < lim2(!isRow, idx1); field += inc(isRow)) {
					if (field == f1 || field == f3) continue;
					pGrid.setImpossible(field, val);
				}
				
				for (int field = idx2*inc(!isRow); field < lim2(!isRow, idx2); field += inc(isRow)) {
					if (field == f2 || field == f4) continue;
					pGrid.setImpossible(field, val);
				}
			}
			buck.clear();
		}
	}

	private int lim1(boolean isRow) {
		return inc(isRow)*n;
	}

	private int lim2(boolean isRow, int rowIdx) {
		return isRow ? rowIdx + n : n*n;
	}

	private int inc(boolean isRow) {
		return isRow ? n : 1;
	}
	
}
