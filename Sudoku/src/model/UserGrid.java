package model;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import model.util.Pair;

public class UserGrid extends Grid {

	private ArrayDeque<Integer>[][] rows;
	private ArrayDeque<Integer>[][] cols;
	private ArrayDeque<Integer>[][] boxes;

	public UserGrid(Grid grid) {
		super(grid);
		initPossibleValues();
	}

	public UserGrid(int k) {
		super(k);
		initPossibleValues();
	}

	public UserGrid(String input) throws IOException {
		super(input);
		initPossibleValues();
	}

	public UserGrid(Reader input) throws IOException {
		super(input);
		initPossibleValues();
	}

	@SuppressWarnings("unchecked")
	private void initPossibleValues() {
		rows = new ArrayDeque[size()][size()];
		cols = new ArrayDeque[size()][size()];
		boxes = new ArrayDeque[size()][size()];

		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[i].length; j++) {
				rows[i][j] = new ArrayDeque<Integer>();
				cols[i][j] = new ArrayDeque<Integer>();
				boxes[i][j] = new ArrayDeque<Integer>();
			}
		}

		for (int i = 0; i < numberOfFields(); i++) {
			int x = get(i);

			if (x != 0) {
				x--;
				rows[rowFor(i)][x].add(i);
				cols[colFor(i)][x].add(i);
				boxes[boxFor(i)][x].add(i);
			}
		}
	}

	@Override
	public Pair<Set<Integer>, Set<Integer>> set(int row, int col, int val) {
		return set(row * size() + col, val);
	}

	@Override
	public Pair<Set<Integer>, Set<Integer>> set(int i, int val) {
		int old = get(i);
		super.set(i, val);

		HashSet<Integer> conflicting = new HashSet<Integer>();
		HashSet<Integer> resolved = new HashSet<Integer>();

		final int row = rowFor(i);
		final int col = colFor(i);
		final int box = boxFor(i);

		if (old != 0) {
			old--;

			ArrayDeque<Integer> r = rows[row][old];
			ArrayDeque<Integer> c = cols[col][old];
			ArrayDeque<Integer> b = boxes[box][old];

			r.remove(i);
			c.remove(i);
			b.remove(i);
			
			if (r.size() == 1) {
				if (!inConflict(r.peek())) {
					resolved.add(r.peek());
				}
			}

			if (c.size() == 1) {
				if (!inConflict(c.peek())) {
					resolved.add(c.peek());
				}
			}

			if (b.size() == 1) {
				if (!inConflict(b.peek())) {
					resolved.add(b.peek());
				}
			}
			
			if (r.size() == 1 && c.size() == 1 && b.size() == 1) {
				resolved.add(i);
			}
		}

		if (val == 0) {
			return new Pair<Set<Integer>, Set<Integer>>(conflicting, resolved);
		}

		val--;

		ArrayDeque<Integer> r = rows[row][val];
		ArrayDeque<Integer> c = cols[col][val];
		ArrayDeque<Integer> b = boxes[box][val];

		r.push(i);
		if (r.size() > 1) {
			conflicting.addAll(r);
		}

		c.push(i);
		if (c.size() > 1) {
			conflicting.addAll(c);
		}

		b.push(i);
		if (b.size() > 1) {
			conflicting.addAll(b);
		}

		return new Pair<Set<Integer>, Set<Integer>>(conflicting, resolved);
	}

	private boolean inConflict(int i) {
		int x = get(i);

		if (x == 0) {
			return false;
		}

		x--;

		return rows[rowFor(i)][x].size() > 1 || cols[colFor(i)][x].size() > 1
				|| boxes[boxFor(i)][x].size() > 1;

	}
}
