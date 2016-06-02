package model;

import model.tactics.AlwaysTactic;
import model.tactics.BoxTactic;
import model.tactics.ChoiceTactic;
import model.tactics.ColTactic;
import model.tactics.IncrementalTwinsTactic;
import model.tactics.XWingTactic;
import model.tactics.RowTactic;
import model.tactics.TwinsTactic;
import model.tactics.UniqueCandidateTactic;
import model.tactics.UnsolvableException;
import model.util.IntPriorityQueue;

public class Solver {

	public Grid grid;
	private IntPriorityQueue pq;
	private PossibleValues[] pvs;
	private PossibleValuesGrid pGrid;
	
	boolean foundSolution = true;

	private AlwaysTactic[] alwaysTactics;
	private ChoiceTactic[] choiceTactics;

	public Solver(Grid grid) {
		this.grid = grid;
	}

	public Grid solve() {
		Grid g = new Grid(grid);
		
		this.pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		
		pvs = g.findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}

		pGrid = new PossibleValuesGrid(g, pvs, pq);

		alwaysTactics = new AlwaysTactic[] { new RowTactic(g, pGrid), new ColTactic(g, pGrid),
				new BoxTactic(g, pGrid), new IncrementalTwinsTactic(g, pGrid) };

		choiceTactics = new ChoiceTactic[] { new UniqueCandidateTactic(g, pGrid), new TwinsTactic(g, pGrid),
				new XWingTactic(g, pGrid) };

		return solve_helper(g);
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
				if (foundSolution) {
					return sol;
				} else {
					foundSolution = true;
				}
			}

			pGrid.revert();
		}

		g.set(field, 0);
		pvs[field] = pv;
		pq.insert(field, pv.possible());

		pGrid.revert();

		return null;
	}
	
	public boolean unique() {
		foundSolution = false;
		boolean ret = solve() == null && foundSolution;
		foundSolution = true;
		return ret;
	}

	protected void showGrid(Grid g) {
		// This method does nothing but is overridden in GuiSolver to display
		// the grid.
	}
}
