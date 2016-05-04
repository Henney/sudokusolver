package model;

import java.util.ArrayDeque;

import model.util.IntPriorityQueue;

public class Solver {

	private Grid grid;
	private IntPriorityQueue pq;
	private ArrayDeque<Integer> changed = new ArrayDeque<Integer>();
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

	private Grid solve_helper(Grid g) {
		if (pq.isEmpty()) {
			return g;
		}

		int field = pq.extractMin();
		
		PossibleValues pv = pvs[field];
		
		if (pv.possible() == 0) {
			pq.insert(field, 0);
			return null;
		}
		
		pvs[field] = null;
		
		int x = 0;
		
		final int n = g.size();
		final int k = grid.k();

		final int row = field / n;
		final int col = field % n;
		
		final int rown = row * n;

		final int boxRow = (row / k) * k;
		final int boxCol = (col / k) * k;

		while (pv.nextAfter(x) != 0) {
			x = pv.nextAfter(x);
			
			int numberChanged = 0;

			// UPDATE

			g.set(field, x);
		
			for (int r = 0; r < n; r++) {
				int i = r * n + col;

				if (pvs[i] != null && pvs[i].set(x, false)) {
					changed.push(i);
					numberChanged++;
					pq.changePrio(i, pvs[i].possible());
				}
			}

			for (int c = 0; c < n; c++) {
				int i = rown + c;

				if (pvs[i] != null && pvs[i].set(x, false)) {
					changed.push(i);
					numberChanged++;
					pq.changePrio(i, pvs[i].possible());
				}
			}

			for (int r = boxRow; r < boxRow+k; r++) {
				int rn = r * n;
				
				for (int c = boxCol; c < boxCol+k; c++) {
					int i = rn + c;

					if (pvs[i] != null && pvs[i].set(x, false)) {
						changed.push(i);
						numberChanged++;
						pq.changePrio(i, pvs[i].possible());
					}
				}
			}

			Grid sol = solve_helper(g);

			if (sol != null) {
				return sol;
			}

			// REVERT

			while (numberChanged-- > 0) {
				int i = changed.pop();
				pvs[i].set(x, true);
				pq.changePrio(i, pvs[i].possible());
			}
		}

		g.set(field, 0);		
		pvs[field] = pv;
		pq.insert(field, pv.possible());

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
