package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import model.util.IntPriorityQueue;
import model.util.Pair;
import model.util.SolvableCallable;

public class PuzzleGenerator {	
	
	static Random generateGenerator;
	static Random minimiseGenerator;
	
	public static Grid generate(int k, long seed) {
		generateGenerator = new Random(seed);
		minimiseGenerator = new Random(seed+1000);

		Grid g = randomBoard(k);
		minimise(g);

		return g;
	}

	private static Grid randomBoard(int k) {
		ExecutorService es1 = Executors.newFixedThreadPool(1);
		ExecutorService es2 = Executors.newFixedThreadPool(1);
		
		Grid g = new Grid(k);
		
		int randAmount = 0;
		switch(g.k()) {
		case 2: randAmount = 5; break;
		case 3: randAmount = 22; break;
		case 4: randAmount = 80; break;
		default: randAmount = 181; break; // k = 5+
		}

		Pair<Grid, LinkedList<Integer>> pair = fillRandom(g, randAmount);
		g = pair.fst;
		LinkedList<Integer> fields = pair.snd;

		PossibleValues[] pvs = g.findPossibleValues();
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		PossibleValuesGrid pGrid = new PossibleValuesGrid(g, pvs, pq);
		
		while (!fields.isEmpty()) {
			int timeout = 100;
			int field = fields.remove();
			int[] origPos = Arrays.copyOf(pvs[field].possibilities(), pvs[field].possibilities().length);

			while (true) {
				if (pvs[field].possible() == 0) {
					timeout *= 2;
					for (int p : origPos) {
						pvs[field].set(p, true);
					}
				}
				
				int idx = (int) (generateGenerator.nextDouble()*pvs[field].possible());
				int val = pvs[field].possibilities()[idx];

//				System.out.println("Field: " + field + " trying val: " + val + " Timeout: " + timeout);
				
				g.set(field, val);
				TacticSolver s1 = new TacticSolver(g);
				SATSolver s2 = new SATSolver(g);
				
				Future<Boolean> task1 = es1.submit(new SolvableCallable(s1, timeout));
				Future<Boolean> task2 = es2.submit(new SolvableCallable(s2, timeout));
				
				while (!task1.isDone() && !task2.isDone()) {
				}

				boolean solvable = false;
				try {
					if (task1.isDone() && task2.isDone()) {
						solvable = task1.get() || task2.get();
					} else if (task1.isDone()) {
						solvable = task1.get();
					} else {
						solvable = task2.get();
					}
				} catch (InterruptedException e) {
					// Shouldn't happen
					e.printStackTrace();
				} catch (ExecutionException e) {
					// Shouldn't happen
					e.printStackTrace();
				}
				
				s1.cancel();
				s2.cancel();
				task1.cancel(true);
				task2.cancel(true);
				
				if (solvable) {
					pGrid.setConnectedImpossible(field, val);
					break;
				}
				
				pvs[field].set(val, false);
				g.set(field, 0);
			}
		}
		
		es1.shutdown();
		es2.shutdown();
		
		return g;
	}

	private static Pair<Grid, LinkedList<Integer>> fillRandom(Grid g, int randAmount) {
		LinkedList<Integer> fields;
		Solver s;
		Grid newG;
		do {
			Grid rG = new Grid(g);
			PossibleValues[] pvs = rG.findPossibleValues();
			IntPriorityQueue pq = new IntPriorityQueue(rG.numberOfFields(), rG.size());
			for (int i = 0; i < pvs.length; i++) {
				pq.insert(i, pvs[i].possible());
			}
			PossibleValuesGrid pGrid = new PossibleValuesGrid(rG, pvs, pq);
			
			fields = randomIntList(g.numberOfFields(), generateGenerator);
			
			for (int i = 0; i < randAmount; i++) {
				int field = fields.remove();
				
				if (pvs[field].possible() == 0) {
					rG = null;
					break;
				}
				
				int idx = (int) (generateGenerator.nextDouble()*pvs[field].possible());
				int val = pvs[field].possibilities()[idx];
				
				rG.set(field, val);
				pGrid.setBoxImpossible(field, val);
				pGrid.setRowImpossible(field, val);
				pGrid.setColImpossible(field, val);
			}
			newG = rG;
			s = new TacticSolver(rG);
		} while (!s.solvableWithTimeout(10000));
		// TODO: use both SAT and tactics here to determine solvability?
		
		return new Pair<Grid, LinkedList<Integer>>(newG, fields);
	}

	private static Grid minimise(Grid g) {
		LinkedList<Integer> fields = randomIntList(g.numberOfFields(), minimiseGenerator);
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
	
	private static LinkedList<Integer> randomIntList(int amount, Random rand) { // TODO: ArrayDeque?
		LinkedList<Integer> fields = new LinkedList<Integer>();
		for (int i = 0; i < amount; i++) {
			fields.add(i);
		}
		Collections.shuffle(fields, rand);
		return fields;
	}
	
}
