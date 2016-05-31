package model;

import java.util.ArrayDeque;
import java.util.function.BiFunction;

import model.tactics.BoxTactic;
import model.tactics.ColTactic;
import model.tactics.IncrementalTwinsTactic;
import model.tactics.RowTactic;
import model.tactics.Tactic;
import model.tactics.TwinsTactic;
import model.tactics.UniqueCandidateTactic;
import model.tactics.UnsolvableException;
import model.util.IntLinkedList;
import model.util.IntPriorityQueue;
import model.util.Node;
import model.util.Pair;

public class Solver {

	public Grid grid;
	private IntPriorityQueue pq;
	private ArrayDeque<Pair<Integer, Integer>> changed;
	private ArrayDeque<Pair<Integer, ArrayDeque<Integer>>> uniqueCandChanged;
	private PossibleValues[] pvs;
	private PossibleValuesGrid pGrid;

	private Tactic[] alwaysTactics;
	private Tactic[] choiceTactics;

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

		pGrid = new PossibleValuesGrid(grid, pvs, pq);

		alwaysTactics = new Tactic[] { new RowTactic(grid, pGrid), new ColTactic(grid, pGrid),
				new BoxTactic(grid, pGrid), new IncrementalTwinsTactic(grid, pGrid)
				};

		choiceTactics = new Tactic[] { new UniqueCandidateTactic(grid, pGrid)
//				, new TwinsTactic(grid, pGrid)
				};

		return solve_helper(new Grid(grid));
	}

	protected Grid solve_helper(Grid g) {
		if (pq.isEmpty()) {
			return g;
		}

		if (!pq.valuesWithPrio(0).isEmpty()) {
			return null;
		}

		pGrid.newTransaction();

		try {
			for (Tactic t : choiceTactics) {
				if (!pq.valuesWithPrio(0).isEmpty()) {
					throw new UnsolvableException();
				} else if (pq.valuesWithPrio(1).isEmpty()) {
					t.apply(-1, -1); // TODO!
				} else {
					break;
				}
			}
		} catch (UnsolvableException e) {
			pGrid.cancelTransaction();
			return null;
		}

		pGrid.endTransaction();

		if (!pq.valuesWithPrio(0).isEmpty()) {
			pGrid.revert();
			return null;
		}

		int field = pq.extractMin();

		PossibleValues pv = pvs[field];

		pvs[field] = null;

		int x = 0;

		while (pv.nextAfter(x) != 0) {
			x = pv.nextAfter(x);

			// UPDATE

			g.set(field, x);

			showGrid(g);

			pGrid.newTransaction();

			try {
				for (Tactic t : alwaysTactics) {
					t.apply(field, x);
				}
			} catch (UnsolvableException e) {
				e.printStackTrace();
				// TODO: impossible right now, but not necessarily
			}

			pGrid.endTransaction();

			Grid sol = solve_helper(g);

			if (sol != null) {
				return sol;
			}

			pGrid.revert();
		}

		g.set(field, 0);
		pvs[field] = pv;
		pq.insert(field, pv.possible());

		pGrid.revert();

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

			for (int f : grid.iterRow(row)) {
				rowPossible.set(f, false);
			}

			for (int col = 0; col < n; col++) {
				PossibleValues p = pvs[row * n + col];
				if (p != null) {
					p.and(rowPossible);
				}
			}
		}

		for (int col = 0; col < n; col++) {
			PossibleValues colPossible = new PossibleValues(n);

			for (int f : grid.iterCol(col)) {
				colPossible.set(f, false);
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
			final int startRow = (box / k) * k;
			final int startCol = (box % k) * k;

			PossibleValues boxPossible = new PossibleValues(n);

			for (int f : grid.iterBox(box)) {
				boxPossible.set(f, false);
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

	protected void showGrid(Grid g) {
		// This method does nothing but is overridden in GuiSolver to display
		// the grid.
	}
}
