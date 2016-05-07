package model;

import java.util.ArrayDeque;
import java.util.function.BiFunction;

import model.util.IntLinkedList;
import model.util.IntPriorityQueue;
import model.util.Node;
import model.util.Pair;

public class Solver {

	private Grid grid;
	private IntPriorityQueue pq;
	private ArrayDeque<Pair<Integer, Integer>> changed;
	private ArrayDeque<Pair<Integer, ArrayDeque<Integer>>> uniqueCandChanged;
	private PossibleValues[] pvs;

	// TODO: this can probably be static
	private ArrayDeque<Integer>[] buckets;
	private ArrayDeque<Integer>[] rows;
	private ArrayDeque<Integer>[] cols;
	private ArrayDeque<Integer>[] boxes;
	
	private final int n;
	private final int k;

	@SuppressWarnings("unchecked")
	public Solver(Grid grid) {
		this.grid = grid;
		
		this.n = grid.size();
		this.k = grid.k();
		
		this.pq = new IntPriorityQueue(grid.numberOfFields(), grid.size());
		this.changed = new ArrayDeque<Pair<Integer, Integer>>();
		this.uniqueCandChanged = new ArrayDeque<Pair<Integer, ArrayDeque<Integer>>>();

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

	public Grid solve() {
		pvs = findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}

		return solve_helper(new Grid(grid));
	}

	private int twins(Grid g) {
		for (int i = 0; i < buckets.length; i++) {
			buckets[i].clear();
		}

		IntLinkedList prio2s = pq.valuesWithPrio(2);

		Node head = prio2s.peek();

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

		int numberChanged = 0;

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

			int changedRows = updateBucket(rows, (field, x) -> setRowImpossible(field, x));
			if (changedRows == -1) {
				revert(numberChanged);
				return -1;
			}

			numberChanged += changedRows;

			int changedCols = updateBucket(cols, (field, x) -> setColImpossible(field, x));
			if (changedCols == -1) {
				revert(numberChanged);
				return -1;
			}

			numberChanged += changedCols;

			int changedBoxes = updateBucket(boxes, (field, x) -> setBoxImpossible(field, x));
			if (changedBoxes == -1) {
				revert(numberChanged);
				return -1;
			}

			numberChanged += changedBoxes;
		}

		return numberChanged;
	}

	private int updateBucket(ArrayDeque<Integer>[] bucket, BiFunction<Integer, Integer, Integer> setImpossible) {
		int numberChanged = 0;

		for (int i = 0; i < bucket.length; i++) {
			if (bucket[i].size() < 2) {
				continue;
			} else if (bucket[i].size() > 2) {
				revert(numberChanged);
				return -1;
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

//				numberChanged += setImpossible.apply(f1, x);
//				numberChanged += setImpossible.apply(f1, y);

				pvs[f1] = pv1;
				pvs[f2] = pv2;
			}
		}

		return numberChanged;
	}

	private int setRowImpossible(int field, int x) {
		final int row = field / n;
		final int startField = row * n;

		int numberChanged = 0;

		for (int c = 0; c < n; c++) {
			int i = startField + c;

			if (pvs[i] != null && pvs[i].set(x, false)) {
				changed.push(new Pair<Integer, Integer>(i, x));
				numberChanged++;
				pq.changePrio(i, pvs[i].possible());
			}
		}

		return numberChanged;
	}

	private int setColImpossible(int field, int x) {
		final int col = field % n;

		int numberChanged = 0;

		for (int r = 0; r < n; r++) {
			int i = r * n + col;

			if (pvs[i] != null && pvs[i].set(x, false)) {
				changed.push(new Pair<Integer, Integer>(i, x));
				numberChanged++;
				pq.changePrio(i, pvs[i].possible());
			}
		}

		return numberChanged;
	}

	private int setBoxImpossible(int field, int x) {
		final int row = field / n;
		final int col = field % n;

		final int boxRow = (row / k) * k;
		final int boxCol = (col / k) * k;

		int numberChanged = 0;

		for (int r = boxRow; r < boxRow + k; r++) {
			int rn = r * n;

			for (int c = boxCol; c < boxCol + k; c++) {
				int i = rn + c;

				if (pvs[i] != null && pvs[i].set(x, false)) {
					changed.push(new Pair<Integer, Integer>(i, x));
					numberChanged++;
					pq.changePrio(i, pvs[i].possible());
				}
			}
		}

		return numberChanged;
	}

	private int setConnectedImpossible(int field, int x) {
		return setRowImpossible(field, x) + setColImpossible(field, x) + setBoxImpossible(field, x);
	}

	private void revert(int count) {
		while (count-- > 0) {
			Pair<Integer, Integer> p = changed.pop();
			int i = p.fst;
			int v = p.snd;
			pvs[i].set(v, true);
			pq.changePrio(i, pvs[i].possible());
		}
	}
	
	private int uniqueCandidate() {
		int numberChanged = 0;
		ArrayDeque<Integer>[][] rowBucket = new ArrayDeque[n][n];
		ArrayDeque<Integer>[][] colBucket = new ArrayDeque[n][n];
		ArrayDeque<Integer>[][] boxBucket = new ArrayDeque[n][n];
		
		// Initialize buckets TODO: should probably be fields
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				rowBucket[i][j] = new ArrayDeque<Integer>();
				colBucket[i][j] = new ArrayDeque<Integer>();
				boxBucket[i][j] = new ArrayDeque<Integer>();
			}
		}
		
		for (int index = 0; index < n*n; index++) {
			PossibleValues pv = pvs[index];
			int row = index/n;
			int col = index%n;
			int box = row/k*k+col/k;
			for (int j = 0; j < n; j++) {
				if (pv != null && pv.get(j+1)) {
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
				ArrayDeque[] bucks = { rowBucket[rcbIndex][value], colBucket[rcbIndex][value], boxBucket[rcbIndex][value] };
				for (ArrayDeque<Integer> buck : bucks) {
					if (buck.size() == 1) {
						int index = buck.removeFirst();
						ArrayDeque<Integer> oldPos = pvs[index].setOnlyPossible(value+1);
						if (oldPos == null) {
							revertUniqueCand(numberChanged);
							return -1;
						}
						uniqueCandChanged.push(new Pair<Integer, ArrayDeque<Integer>>(index, oldPos));
						pq.changePrio(index, 1);
						numberChanged++;
					}
				}
			}
		}
		return numberChanged;
	}

	private void revertUniqueCand(int amount) {
		for (int i = 0; i < amount; i++) {
			Pair<Integer, ArrayDeque<Integer>> item = uniqueCandChanged.pop();
			for (int val : item.snd) {
				pvs[item.fst].set(val, true);
			}
			pq.changePrio(item.fst, pvs[item.fst].possible());
		}
	}

	private Grid solve_helper(Grid g) {
		if (pq.isEmpty()) {
			return g;
		}
		
		// TODO: peek in pq before running twins
		
		int uniqueCandChanged = 0;
		if (pq.valuesWithPrio(1).isEmpty()) {
			uniqueCandChanged = uniqueCandidate();
		}
		if (uniqueCandChanged == -1) {
			return null;
		}

		/*int twinsChanged = twins(g);

		if (twinsChanged == -1) {
			return null;
		}*/

		int field = pq.extractMin();

		PossibleValues pv = pvs[field];

		if (pv.possible() == 0) {
			pq.insert(field, 0);
			revertUniqueCand(uniqueCandChanged);
			return null;
		}

		pvs[field] = null;

		int x = 0;

		while (pv.nextAfter(x) != 0) {
			x = pv.nextAfter(x);

			// UPDATE

			g.set(field, x);

			int numberChanged = setConnectedImpossible(field, x);

			Grid sol = solve_helper(g);

			if (sol != null) {
				return sol;
			}

			revert(numberChanged);
		}

		g.set(field, 0);
		pvs[field] = pv;
		pq.insert(field, pv.possible());

		//revert(twinsChanged);
		revertUniqueCand(uniqueCandChanged);

		return null;
	}

	public PossibleValues[] findPossibleValues() {
		final int n = grid.size();

		final PossibleValues[] pvs = new PossibleValues[grid.numberOfFields()];

		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if (grid.get(row, col) == 0) {
					pvs[row * n + col] = new PossibleValues(n);
				}
			}
		}

		for (int row = 0; row < n; row++) {
			PossibleValues rowPossible = new PossibleValues(n);

			for (int col = 0; col < n; col++) {
				rowPossible.set(grid.get(row, col), false);
			}

			for (int col = 0; col < n; col++) {
				PossibleValues p = pvs[row * n + col];
				if (p != null) {
					p.and(rowPossible);
				}
			}
		}

		// TODO: do something about the redundancy?
		for (int col = 0; col < n; col++) {
			PossibleValues colPossible = new PossibleValues(n);

			for (int row = 0; row < n; row++) {
				colPossible.set(grid.get(row, col), false);
			}

			for (int row = 0; row < n; row++) {
				PossibleValues p = pvs[row * n + col];
				if (p != null) {
					p.and(colPossible);
				}
			}
		}

		final int k = grid.k();

		for (int box = 0; box < n; box++) {
			int startRow = (box / k) * k;
			int startCol = (box % k) * k;

			PossibleValues boxPossible = new PossibleValues(n);

			for (int dRow = 0; dRow < k; dRow++) {
				for (int dCol = 0; dCol < k; dCol++) {

					boxPossible.set(grid.get(startRow + dRow, startCol + dCol), false);
				}
			}

			for (int dRow = 0; dRow < k; dRow++) {
				for (int dCol = 0; dCol < k; dCol++) {
					int row = startRow + dRow;
					int col = startCol + dCol;

					PossibleValues p = pvs[row * n + col];
					if (p != null) {
						p.and(boxPossible);
					}
				}
			}
		}

		return pvs;
	}

}
