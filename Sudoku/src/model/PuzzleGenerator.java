package model;

import java.util.Collections;
import java.util.LinkedList;

public class PuzzleGenerator {
	
	public static Grid generate(int k) {
		Grid g = randomBoard(new Grid(k), 0);
		
		LinkedList<Integer> fields = new LinkedList<Integer>();
		for (int i = 0; i < g.numberOfFields(); i++) {
			fields.add(i);
		}
		Collections.shuffle(fields);
		
		minimise(g, fields);
		
		return g;
	}

	private static Grid minimise(Grid g, LinkedList<Integer> fields) {
		int timeout = g.k()*100;
		while (!fields.isEmpty()) {
			int field = fields.remove();
			int prevVal = g.get(field);
			g.set(field, 0);
			
			Solver s = new Solver(g);
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

	private static Grid randomBoard(Grid g, int field) {
		if (field == g.numberOfFields()) return g;
		PossibleValues[] pvs = g.findPossibleValues();
		
		int[] origPos = pvs[field].possibilities();
		int timeout = g.k()*100;
		
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
			Solver s = new Solver(g);
			if (s.solveWithTimeout(timeout) != null) {
				break;
			}
			pvs[field].set(val, false);
			g.set(field, 0);
		}

		return randomBoard(g, field+1);
	}
	
}
