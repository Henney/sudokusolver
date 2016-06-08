package model;

import java.util.Collections;
import java.util.LinkedList;

import model.tactics.AlwaysTactic;
import model.tactics.BoxTactic;
import model.tactics.ColTactic;
import model.tactics.IncrementalTwinsTactic;
import model.tactics.RowTactic;
import model.util.IntPriorityQueue;

public class PuzzleGenerator {
	
	public static Grid generate(int k) {
		LinkedList<Integer> fields = randomIntList(k);
		Grid g = randomBoard(new Grid(k), fields);
		
		fields = randomIntList(k);
		minimise(g, fields);
		
		return g;
	}
	
	private static LinkedList<Integer> randomIntList(int k) {
		int n = k*k;
		LinkedList<Integer> fields = new LinkedList<Integer>();
		for (int i = 0; i < n*n; i++) {
			fields.add(i);
		}
		Collections.shuffle(fields);
		return fields;
	}

	private static Grid randomBoard(Grid g, LinkedList<Integer> fields) {
		int timeout = g.k()*100;
		
		PossibleValues[] pvs;
		IntPriorityQueue pq;
		PossibleValuesGrid pGrid;
		AlwaysTactic[] alwaysTactics;
		
		while (!fields.isEmpty()) {
			pvs = g.findPossibleValues();
			pq = new IntPriorityQueue(g.numberOfFields(), g.size());
			for (int i = 0; i < pvs.length; i++) {
				if (pvs[i] != null) {
					pq.insert(i, pvs[i].possible());
				}
			}
			pGrid = new PossibleValuesGrid(g, pvs, pq);
			alwaysTactics = new AlwaysTactic[] { new RowTactic(g, pGrid), new ColTactic(g, pGrid),
							new BoxTactic(g, pGrid) };

			int field = fields.remove();
			int[] origPos = pvs[field].possibilities();
			
			while (true) {
				if (pvs[field].possible() == 0) {
					timeout *= 2;
					for (int p : origPos) {
						pvs[field].set(p, true);
					}
				}
				
				int idx = (int) (Math.random()*pvs[field].possible());
				int val = pvs[field].possibilities()[idx];
				
				g.set(field, val);
				TacticSolver s = new TacticSolver(g);
				if (s.solveWithTimeout(timeout) != null) {  // TODO: do we need to run tactics to remove possibilities?
					for (AlwaysTactic t : alwaysTactics) {
						t.apply(field, val);
					}
					break;
				}
				pvs[field].set(val, false);
				g.set(field, 0);
			}
		}
		
		return g;
	}

	private static Grid minimise(Grid g, LinkedList<Integer> fields) {
		int timeout = g.k()*100;
		while (!fields.isEmpty()) {
			int field = fields.remove();
			int prevVal = g.get(field);
			g.set(field, 0);
			
			TacticSolver s = new TacticSolver(g);
			long b = System.currentTimeMillis();
			if (!s.uniqueWithTimeout(timeout)) {
				g.set(field, prevVal);
			}
			long a = System.currentTimeMillis();
			
			if (a-b > timeout*10) {
				return g;
			}
		}
		return g;
	}
	
}
