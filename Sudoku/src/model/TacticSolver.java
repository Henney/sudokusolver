package model;

import model.tactics.AlwaysTactic;
import model.tactics.ChoiceTactic;
import model.tactics.IncrementalTwinsTactic;
import model.tactics.XWingTactic;
import model.tactics.TwinsTactic;
import model.tactics.UniqueCandidateTactic;
import model.tactics.UnsolvableException;
import model.util.IntPriorityQueue;

public class TacticSolver extends Solver {

	private IntPriorityQueue pq;
	private PossibleValues[] pvs;
	private PossibleValuesGrid pGrid;
	
	boolean foundSolution = true;
	int timeout = 0;
	long start;

	private AlwaysTactic[] alwaysTactics;
	private ChoiceTactic[] choiceTactics;
	
	public TacticSolver(Grid g) {
		super(g);
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

		alwaysTactics = new AlwaysTactic[] { new IncrementalTwinsTactic(g, pGrid) };

		choiceTactics = new ChoiceTactic[] { new UniqueCandidateTactic(g, pGrid), new TwinsTactic(g, pGrid),
				new XWingTactic(g, pGrid) };
		
		return solveHelper(g);
	}

	private Grid solveHelper(Grid g) {
		long ct = System.currentTimeMillis();
		if (start != 0 && ct-start > timeout) {
			return null;
		}
		
		if (!run) {
			return grid;
		}
		
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

			showField(field, x);

			pGrid.newTransaction();

			pGrid.setRowImpossible(field, x);
			pGrid.setColImpossible(field, x);
			pGrid.setBoxImpossible(field, x);
			
			for (AlwaysTactic t : alwaysTactics) {
				t.apply(field, x);
			}

			pGrid.endTransaction();

			Grid sol = solveHelper(g);

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

		hideField(field);
		
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
	
	public Grid solveWithTimeout(int t) {
		start = System.currentTimeMillis();
		timeout = t;	
		Grid g = solve();
		timeout = 0;
		return g;
	}

	public boolean uniqueWithTimeout(int t) {
		timeout = t;
		boolean ret = unique();
		timeout = 0;
		return ret;
	}
	
	public boolean solvableWithTimeout(int t) { // TODO: Only used for testing board generation randomness. Maybe delete after?
		start = System.currentTimeMillis();
		timeout = t;	
		boolean b = solvable();
		timeout = 0;
		return b;
	}

	protected void showField(int field, int val) {
		// This method does nothing but is overridden in GuiSolver to display
		// the grid.
	}

	protected void hideField(int field) {
		// This method does nothing but is overridden in GuiSolver to display
		// the grid.
	}
	
}
