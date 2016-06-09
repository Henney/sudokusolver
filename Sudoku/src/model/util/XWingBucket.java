package model.util;

import java.util.ArrayDeque;

public class XWingBucket {

	int size;
	private ArrayDeque<Pair<Integer, Integer>>[][] bucket;
	private ArrayDeque<Pair<Pair<Integer, Integer>,
	  						Pair<Integer, Integer>>> best;

	@SuppressWarnings("unchecked")
	public XWingBucket(int size) {
		this.size = size;
		this.bucket = new ArrayDeque[size][size];
		initBucket();
		this.best = new ArrayDeque<Pair<Pair<Integer, Integer>,
				  						Pair<Integer, Integer>>>();
	}

	public void add(int col1, int col2, int i, int j) {
		Pair<Integer, Integer> item = new Pair<Integer, Integer>(i, j);
		bucket[col1][col2].add(item);
		if (bucket[col1][col2].size() == 2) {
			Pair<Integer, Integer> p1 = bucket[col1][col2].poll();
			Pair<Integer, Integer> p2 = bucket[col1][col2].poll();
			best.add(new Pair<Pair<Integer, Integer>,
							  Pair<Integer, Integer>>(p1, p2));
		}
	}
	
	private void initBucket() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				bucket[i][j] = new ArrayDeque<Pair<Integer, Integer>>();
			}
		}
	}
	
	public ArrayDeque<Pair<Pair<Integer, Integer>,
	  					   Pair<Integer, Integer>>> getBest() {
		return best;
	}
	
	public void clear() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				bucket[i][j].clear();
			}
		}
		best.clear();
	}
}
