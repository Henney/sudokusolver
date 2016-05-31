package model.util;

import java.util.ArrayDeque;

public class XWingBucket {

	private ArrayDeque<Pair<Integer, Integer>>[][] bucket;
	private ArrayDeque<Pair<Pair<Integer, Integer>,
	  						Pair<Integer, Integer>>> best;

	public XWingBucket(int size) {
		this.bucket = new ArrayDeque[size][size];
		this.best = new ArrayDeque<Pair<Pair<Integer, Integer>,
				  						Pair<Integer, Integer>>>();
	}

	public void add(int col1, int col2, int i, int j) {
		Pair<Integer, Integer> item = new Pair<Integer, Integer>(i, j);
		bucket[col1][col2].add(item);
		if (bucket[i][j].size() == 2) {
			Pair<Integer, Integer> p1 = bucket[i][j].poll();
			Pair<Integer, Integer> p2 = bucket[i][j].poll();
			best.add(new Pair<Pair<Integer, Integer>,
							  Pair<Integer, Integer>>(p1, p2));
		}
	}
	
	public ArrayDeque<Pair<Pair<Integer, Integer>,
	  					   Pair<Integer, Integer>>> getBest() {
		return best;
	}
}
