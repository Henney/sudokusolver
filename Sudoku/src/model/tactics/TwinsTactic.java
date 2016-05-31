package model.tactics;

import java.util.ArrayDeque;
import java.util.function.BiFunction;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.util.Node;

public class TwinsTactic extends ChoiceTactic {

	private PossibleValues[] pvs;

	private ArrayDeque<Integer>[] buckets;
	private ArrayDeque<Integer>[] rows;
	private ArrayDeque<Integer>[] cols;
	private ArrayDeque<Integer>[] boxes;

	@SuppressWarnings("unchecked")
	public TwinsTactic(Grid grid, PossibleValuesGrid pGrid) {
		super(grid, pGrid);

		pvs = pGrid.getPossibleValues();

		this.buckets = new ArrayDeque[grid.numberOfFields()];

		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new ArrayDeque<Integer>();
		}

		this.rows = new ArrayDeque[grid.size()];
		this.cols = new ArrayDeque[grid.size()];
		this.boxes = new ArrayDeque[grid.size()];

		for (int i = 0; i < grid.size(); i++) {
			rows[i] = new ArrayDeque<Integer>();
			cols[i] = new ArrayDeque<Integer>();
			boxes[i] = new ArrayDeque<Integer>();
		}
	}

	@Override
	public void apply() throws UnsolvableException {
		for (int i = 0; i < buckets.length; i++) {
			buckets[i].clear();
		}

		Node head = pGrid.valuesWithPrio(2).peek();

		while (head != null) {
			int field = head.getValue();

			PossibleValues pv = pvs[field];

			int x = pv.nextAfter(0);
			int y = pv.nextAfter(x);

			x--;
			y--;

			buckets[x * n + y].push(field);

			head = head.getNext();
		}

		for (int i = 0; i < buckets.length; i++) {
			for (int j = 0; j < rows.length; j++) {
				rows[j].clear();
				cols[j].clear();
				boxes[j].clear();
			}

			for (int field : buckets[i]) {
				final int row = field / n;
				final int col = field % n;
				final int box = (row / k) * k + col / k;

				rows[row].add(field);
				cols[col].add(field);
				boxes[box].add(field);
			}

			updateBucket(rows, (field, x) -> { pGrid.setRowImpossible(field, x); return null; });
			updateBucket(cols, (field, x) -> { pGrid.setColImpossible(field, x); return null; });
			updateBucket(boxes, (field, x) -> { pGrid.setBoxImpossible(field, x); return null; });
		}

	}

	private void updateBucket(ArrayDeque<Integer>[] bucket, BiFunction<Integer, Integer, Void> setImpossible) throws UnsolvableException {
				for (int i = 0; i < bucket.length; i++) {
			if (bucket[i].size() < 2) {
				continue;
			} else if (bucket[i].size() > 2) {
				throw new UnsolvableException();
			} else {
				int f1 = bucket[i].pop();
				int f2 = bucket[i].pop();

				PossibleValues pv1 = pvs[f1];
				PossibleValues pv2 = pvs[f2];

				int x = pv1.nextAfter(0);
				int y = pv2.nextAfter(x);

				// don't update these
				pvs[f1] = null;
				pvs[f2] = null;

				setImpossible.apply(f1, x);
				setImpossible.apply(f1, y);

				pvs[f1] = pv1;
				pvs[f2] = pv2;
			}
		}
	}

}
