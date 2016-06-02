package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class PuzzleGenerator {
	
	public PuzzleGenerator() {
	}
	
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
		while (!fields.isEmpty()) {
			int field = fields.remove();
			int prevVal = g.get(field);
			g.set(field, 0);
			
			Solver s = new Solver(g);
			if (!s.unique()) {
				g.set(field, prevVal);
			}
		}
		return g;
	}

	private static Grid randomBoard(Grid g, int field) {
		if (field == g.numberOfFields()) return g;
		PossibleValues[] pvs = g.findPossibleValues(); // TODO optimise this so we don't have to find possible values every time
													   // - maybe not necessary since we have to solve every time anyway
		
		long b = System.currentTimeMillis();
		
		while (true) {
			int idx = (int) (Math.random()*pvs[field].possible());
			int val = pvs[field].possibilities()[idx];
			
			g.set(field, val);
			Solver s = new Solver(g);
			if (s.solve() != null) {
				break;
			}
			long a = System.currentTimeMillis();
			g.set(field, 0);
		}

		return randomBoard(g, field+1);
	}
	
	// TODO: Possibly try generating in some other way?
	private static Grid generate2(int k) {
		Grid g = new Grid(k);
		
		Solver s = new Solver(g);
		while (true) {
			PossibleValues[] pvs = g.findPossibleValues();

			LinkedList<Integer> fields = new LinkedList<Integer>();
			for (int i = 0; i < g.numberOfFields(); i++) {
				fields.add(i);
			}
			Collections.shuffle(fields);
			
			int field = fields.remove();
			
			int idx = (int) (Math.random()*pvs[field].possible());
			int val = pvs[field].possibilities()[idx];
			
			g.set(field, val);
			s = new Solver(g);

			if (s.unique()) {
				g.set(field, 0);
			} else {
				break;
			}
			
			
		}
		
		return g;
	}
	
}
