package model.util;

import java.util.Iterator;

import model.Grid;

public class ColIterator implements Iterable<Integer> {
	final Grid grid;
	final int col;
	final int n;
	
	public ColIterator(Grid g, int c) {
		grid = g;
		col = c;
		n = grid.size();
	}
	
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int row = 0;
			
			@Override
            public boolean hasNext() {
                return row < n;
            }

            @Override
            public Integer next() {
                int ret = grid.get(row, col);
                row++;
                return ret;
            }

		};
	}
}