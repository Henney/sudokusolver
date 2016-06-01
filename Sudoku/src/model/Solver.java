package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.function.BiFunction;

import model.tactics.AlwaysTactic;
import model.tactics.BoxTactic;
import model.tactics.ChoiceTactic;
import model.tactics.ColTactic;
import model.tactics.IncrementalTwinsTactic;
import model.tactics.XWingTactic;
import model.tactics.RowTactic;
import model.tactics.Tactic;
import model.tactics.TwinsTactic;
import model.tactics.UniqueCandidateTactic;
import model.tactics.UnsolvableException;
import model.tactics.XWingTactic;
import model.util.IntLinkedList;
import model.util.IntPriorityQueue;
import model.util.Node;
import model.util.Pair;
import model.util.XWingBucket;

public class Solver {

	public Grid grid;
	private IntPriorityQueue pq;
	private ArrayDeque<Pair<Integer, Integer>> changed;
	private ArrayDeque<Pair<Integer, ArrayDeque<Integer>>> uniqueCandChanged;
	private PossibleValues[] pvs;
	private PossibleValuesGrid pGrid;

	private AlwaysTactic[] alwaysTactics;
	private ChoiceTactic[] choiceTactics;

	public Solver(Grid grid) {
		this.grid = grid;
		this.pq = new IntPriorityQueue(grid.numberOfFields(), grid.size());
	}

	public Grid solve() {
		pvs = grid.findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}

		pGrid = new PossibleValuesGrid(grid, pvs, pq);

		alwaysTactics = new AlwaysTactic[] { new RowTactic(grid, pGrid), new ColTactic(grid, pGrid),
				new BoxTactic(grid, pGrid), new IncrementalTwinsTactic(grid, pGrid) };

		choiceTactics = new ChoiceTactic[] { new UniqueCandidateTactic(grid, pGrid), new TwinsTactic(grid, pGrid),
				new XWingTactic(grid, pGrid) };

		return solve_helper(new Grid(grid));
	}

	private Grid solve_helper(Grid g) {
		if (pq.isEmpty()) {
			return g;
		}

		if (!pq.valuesWithPrio(0).isEmpty()) {
			return null;
		}

		pGrid.newTransaction();

		try {
			int changesMadeLast = -1;
			while (pGrid.changesMadeInTransaction() != changesMadeLast) {
				changesMadeLast = pGrid.changesMadeInTransaction();

				for (ChoiceTactic t : choiceTactics) {
					if (!pq.valuesWithPrio(0).isEmpty()) {
						throw new UnsolvableException();
					} else if (pq.valuesWithPrio(1).isEmpty()) {
						t.apply();
					} else {
						break;
					}
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
				for (AlwaysTactic t : alwaysTactics) {
					t.apply(field, x);
				}
			} catch (UnsolvableException e) {
				pGrid.cancelTransaction();
				continue;
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

	protected void showGrid(Grid g) {
		// This method does nothing but is overridden in GuiSolver to display
		// the grid.
	}
}
