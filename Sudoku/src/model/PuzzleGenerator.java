package model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class PuzzleGenerator {

	private PossibleValues[] pvs;

	public static void main(String[] args) {
//		for (int i = 0; i < 100; i++) {
			new PuzzleGenerator().generate(4);
//		}
	}
	
	public PuzzleGenerator() {
	}
	
	public Grid generate(int k) {
		Grid g = randomBoard(new Grid(k), 0);
		
		LinkedList<Integer> fields = new LinkedList<Integer>();
		for (int i = 0; i < g.numberOfFields(); i++) {
			fields.add(i);
		}
		Collections.shuffle(fields);
		g = minimise(g, fields);
		System.out.println(g);
		
		return g;
	}

	private Grid minimise(Grid g, LinkedList<Integer> fields) {
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

	private Grid randomBoard(Grid g, int field) {
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
			g.set(field, 0);
			if (System.currentTimeMillis()-b > 1000) return null;
		}

		return randomBoard(g, field+1);
	}
	
	
}
