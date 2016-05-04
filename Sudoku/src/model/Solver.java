package model;

import java.util.ArrayDeque;

import model.util.IntLinkedList;
import model.util.IntPriorityQueue;
import model.util.Node;
import model.util.Pair;

public class Solver {

	private Grid grid;
	private IntPriorityQueue pq;
	private ArrayDeque<Pair<Integer, Integer>> changed = new ArrayDeque<Pair<Integer, Integer>>();
	private PossibleValues[] pvs;

	public Solver(Grid grid) {
		this.grid = grid;
		this.pq = new IntPriorityQueue(grid.numberOfFields(), grid.size());
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
	
	@SuppressWarnings("unchecked")
	private int twins(Grid g) {
		// TODO: persist this
		ArrayDeque<Integer>[] buckets = new ArrayDeque[g.numberOfFields()];
		
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new ArrayDeque<Integer>();
		}
		
		IntLinkedList prio2s = pq.valuesWithPrio(2);
		
		Node head = prio2s.peek();
		
		while (head != null) {
			int field = head.getValue();
			
			PossibleValues pv = pvs[field];
			
			int x = pv.nextAfter(0);
			int y = pv.nextAfter(x);
			
			if (x > y) {
				int t = x;
				x = y;
				y = t;
			}
			
			x--;
			y--;
			
			buckets[x*g.size() + y].push(field);
			
			head = head.getNext();
		}
		
		ArrayDeque<Integer>[] rows = new ArrayDeque[g.size()];
		ArrayDeque<Integer>[] cols = new ArrayDeque[g.size()];
		ArrayDeque<Integer>[] boxes = new ArrayDeque[g.size()];
		
		for (int i = 0; i < g.size(); i++) {
			rows[i] = new ArrayDeque<Integer>();
			cols[i] = new ArrayDeque<Integer>();
			boxes[i] = new ArrayDeque<Integer>();
		}
		
		for (int i = 0; i < buckets.length; i++) {
			for (int field : buckets[i]) {
				final int row = field / g.size();
				final int col = field % g.size();
				final int box = (row / g.k()) * g.k() + col / g.k();
				
				rows[row].add(field);
				cols[col].add(field);
				boxes[box].add(field);
			}
		}
		
		int changedRows = updateBucket(rows);
		
		if (changedRows == -1) {
			return -1;
		}
		
		int changedCols = updateBucket(cols);
		
		if (changedCols == -1) {
			return -1;
		}
		
		int changedBoxes = updateBucket(boxes);
		if (changedBoxes == -1) {
			return -1;
		}
			
		return changedRows + changedCols + changedBoxes;
	}
	
	private int updateBucket(ArrayDeque<Integer>[] bucket) {
		int numberChanged = 0;
		
		for (int i = 0; i < bucket.length; i++) {
			if (bucket[i].size() < 2) {
				continue;
			} else if (bucket[i].size() > 2) {
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
				
				
				// There is some overlap in updating the row here
				numberChanged += setConnectedImpossible(f1, x);
				numberChanged += setConnectedImpossible(f1, y);
				
				numberChanged += setConnectedImpossible(f2, x);
				numberChanged += setConnectedImpossible(f2, y);
				
				// revert them
				pvs[f1] = pv1;
				pvs[f2] = pv2;
			}
		}
		
		return numberChanged;
	}
	
	private int setConnectedImpossible(int field, int x) {
		final int n = grid.size();
		final int k = grid.k();

		final int row = field / n;
		final int col = field % n;
		
		final int rown = row * n;

		final int boxRow = (row / k) * k;
		final int boxCol = (col / k) * k;
		
		int numberChanged = 0;
		
		for (int r = 0; r < n; r++) {
			int i = r * n + col;

			if (pvs[i] != null && pvs[i].set(x, false)) {
				changed.push(new Pair<Integer, Integer>(i, x));
				numberChanged++;
				pq.changePrio(i, pvs[i].possible());
			}
		}

		for (int c = 0; c < n; c++) {
			int i = rown + c;

			if (pvs[i] != null && pvs[i].set(x, false)) {
				changed.push(new Pair<Integer, Integer>(i, x));
				numberChanged++;
				pq.changePrio(i, pvs[i].possible());
			}
		}

		for (int r = boxRow; r < boxRow+k; r++) {
			int rn = r * n;
			
			for (int c = boxCol; c < boxCol+k; c++) {
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
	
	private void revert(int count) {
		while (count-- > 0) {
			Pair<Integer, Integer> p = changed.pop();
			int i = p.fst;
			int v = p.snd;
			pvs[i].set(v, true);
			pq.changePrio(i, pvs[i].possible());
		}
	}

	private Grid solve_helper(Grid g) {
		if (pq.isEmpty()) {
			return g;
		}
		
//		int twinsChanged = twins(g);
//		
//		if (twinsChanged == -1) {
//			return null;
//		}

		int field = pq.extractMin();
		
		PossibleValues pv = pvs[field];
		
		if (pv.possible() == 0) {
			pq.insert(field, 0);
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
		
//		revert(twinsChanged);

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
