package model.util;

import java.util.Iterator;

import model.Grid;

public class RowIterator implements Iterable<Integer> {
	final Grid grid;
	final int row;
	final int n;
	
	public RowIterator(Grid g, int r) {
		grid = g;
		row = r;
		n = grid.size();
	}
	
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int col = 0;
			
			@Override
            public boolean hasNext() {
                return col < n;
            }

            @Override
            public Integer next() {
                int ret = grid.get(row, col);
                col++;
                return ret;
            }

		};
	}
}