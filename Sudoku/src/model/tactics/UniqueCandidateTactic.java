package model.tactics;

import java.util.ArrayDeque;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;

public class UniqueCandidateTactic extends Tactic {
	
	ArrayDeque<Integer>[][] rowBucket;
	ArrayDeque<Integer>[][] colBucket;
	ArrayDeque<Integer>[][] boxBucket;
	
	PossibleValues[] pvs;

	@SuppressWarnings("unchecked")
	public UniqueCandidateTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);
		
		pvs = pGrid.getPossibleValues();
		
		this.rowBucket = new ArrayDeque[n][n];
		this.colBucket = new ArrayDeque[n][n];
		this.boxBucket = new ArrayDeque[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				rowBucket[i][j] = new ArrayDeque<Integer>();
				colBucket[i][j] = new ArrayDeque<Integer>();
				boxBucket[i][j] = new ArrayDeque<Integer>();
			}
		}
	}

	@Override
@SuppressWarnings({ "rawtypes", "unchecked" })
	public void apply(int field_, int value_) throws UnsolvableException {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				rowBucket[i][j].clear();
				colBucket[i][j].clear();
				boxBucket[i][j].clear();
			}
		}

		for (int index = 0; index < n * n; index++) {
			PossibleValues pv = pvs[index];
			final int row = index / n;
			final int col = index % n;
			final int box = row / k * k + col / k;
			
			for (int j = 0; j < n; j++) {
				if (pv != null && pv.get(j + 1)) {
					// Syntax: bucket[number][value] = fieldIndex
					rowBucket[row][j].add(index);
					colBucket[col][j].add(index);
					boxBucket[box][j].add(index);
				}
			}
		}

		// rcbIndex is the index for the row/column/box
		for (int rcbIndex = 0; rcbIndex < n; rcbIndex++) {
			for (int value = 0; value < n; value++) {
				// Check if there is only one candidate
				ArrayDeque[] bucks = {
						rowBucket[rcbIndex][value],
						colBucket[rcbIndex][value],
						boxBucket[rcbIndex][value]
				};
				
				for (ArrayDeque<Integer> buck : bucks) {
					if (buck.size() == 1) {
						int index = buck.removeFirst();
						if (!pvs[index].get(value+1)) {
							throw new UnsolvableException();
						}
						
						pGrid.setOnlyPossible(index, value+1);
					}
				}
			}
		}
	}

}
