package model.util;

import java.util.Iterator;

import model.Grid;

public class BoxIterator implements Iterable<Integer> {
	final Grid grid;
	final int box;
	final int k;
	
	public BoxIterator(Grid g, int b) {
		grid = g;
		box = b;
		k = grid.k();
	}
	
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int r = 0;
			private int c = 0;
			
			private final int startRow = (box / k) * k;
			private final int startCol = (box % k) * k;
			
			@Override
            public boolean hasNext() {
                return r < k && c < k;
            }

            @Override
            public Integer next() {
                int ret = grid.get(startRow+r, startCol+c);
                
                c++;
                
                if (c >= k) {
                	c = 0;
                	r++;
                }
                
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
	};}
}