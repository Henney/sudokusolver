package model;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import model.util.Pair;

public class UserGrid {

	private Grid grid;

	private ArrayDeque<Integer>[][] rows;
	private ArrayDeque<Integer>[][] cols;
	private ArrayDeque<Integer>[][] boxes;

	@SuppressWarnings("unchecked")
	private void initPossibleValues() {
		rows = new ArrayDeque[grid.size()][grid.size()];
		cols = new ArrayDeque[grid.size()][grid.size()];
		boxes = new ArrayDeque[grid.size()][grid.size()];

		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[i].length; j++) {
				rows[i][j] = new ArrayDeque<Integer>();
				cols[i][j] = new ArrayDeque<Integer>();
				boxes[i][j] = new ArrayDeque<Integer>();
			}
		}

		for (int i = 0; i < grid.numberOfFields(); i++) {
			int x = grid.get(i);

			if (x != 0) {
				x--;
				rows[grid.rowFor(i)][x].add(i);
				cols[grid.colFor(i)][x].add(i);
				boxes[grid.boxFor(i)][x].add(i);
			}
		}
	}

	public UserGrid(Grid grid) {
		this.grid = grid;
		initPossibleValues();
	}

	public UserGrid(int k) {
		grid = new Grid(k);
		initPossibleValues();
	}

	public UserGrid(String input) throws IOException {
		grid = new Grid(input);
		initPossibleValues();
	}

	public UserGrid(Reader input) throws IOException {
		grid = new Grid(input);
		initPossibleValues();
	}

	public int get(int row, int col) {
		return grid.get(row, col);
	}

	public int get(int i) {
		return grid.get(i);
	}

	public Pair<Set<Integer>, Set<Integer>> set(int row, int col, int val) {
		return set(row * grid.size() + col, val);
	}

	public Pair<Set<Integer>, Set<Integer>> set(int i, int val) {
		int old = grid.get(i);
		grid.set(i, val);

		HashSet<Integer> conflicting = new HashSet<Integer>();
		HashSet<Integer> resolved = new HashSet<Integer>();

		final int row = grid.rowFor(i);
		final int col = grid.colFor(i);
		final int box = grid.boxFor(i);

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
		}

		if (val == 0) {
			return new Pair<Set<Integer>, Set<Integer>>(conflicting, resolved);
		}

		val--;

		ArrayDeque<Integer> r = rows[row][val];
		ArrayDeque<Integer> c = cols[col][val];
		ArrayDeque<Integer> b = boxes[box][val];

		System.out.println(val + ": " + r);

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
		int x = grid.get(i);

		if (x == 0) {
			return false;
		}

		x--;

		return rows[grid.rowFor(i)][x].size() > 1 || cols[grid.colFor(i)][x].size() > 1
				|| boxes[grid.boxFor(i)][x].size() > 1;

	}

	public int k() {
		return grid.k();
	}

	public int size() {
		return grid.size();
	}

	public Grid getGrid() {
		return grid;
	}
}
