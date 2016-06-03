package model;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;

import model.util.BoxIterator;
import model.util.ColIterator;
import model.util.Pair;
import model.util.RowIterator;

public class Grid {

	private int[] grid;
	private int k;
	private int n;
	
	public Grid(int k) {
		this.k = k;
		this.n = k*k;
		this.grid = new int[n*n];
	}
	
	public Grid(Grid other) {
		this.grid = Arrays.copyOf(other.grid, other.grid.length);
		this.k = other.k;
		this.n = other.n;
	}
	
	public Grid(String input) throws IOException {
		this(new StringReader(input));
	}
	
	public Grid(Reader input) throws IOException {
		Pair<int[], Integer> parsed = Parser.parseGrid(input);
		this.grid = parsed.fst;
		this.k = parsed.snd;
		this.n = k*k;
	}

	public PossibleValues[] findPossibleValues() {
		final PossibleValues[] pvs = new PossibleValues[numberOfFields()];

		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if (get(row, col) == 0) {
					pvs[row * n + col] = new PossibleValues(n);
				}
			}
		}

		for (int row = 0; row < n; row++) {
			PossibleValues rowPossible = new PossibleValues(n);

			for (int f : iterRow(row)) {
				rowPossible.set(f, false);
			}

			for (int col = 0; col < n; col++) {
				PossibleValues p = pvs[row * n + col];
				if (p != null) {
					p.and(rowPossible);
				}
			}
		}

		for (int col = 0; col < n; col++) {
			PossibleValues colPossible = new PossibleValues(n);

			for (int f : iterCol(col)) {
				colPossible.set(f, false);
			}

			for (int row = 0; row < n; row++) {
				PossibleValues p = pvs[row * n + col];
				if (p != null) {
					p.and(colPossible);
				}
			}
		}

		for (int box = 0; box < n; box++) {
			final int startRow = (box / k) * k;
			final int startCol = (box % k) * k;

			PossibleValues boxPossible = new PossibleValues(n);

			for (int f : iterBox(box)) {
				boxPossible.set(f, false);
			}

			for (int dRow = 0; dRow < k; dRow++) {
				for (int dCol = 0; dCol < k; dCol++) {
					int row = startRow + dRow;
					int col = startCol + dCol;

					PossibleValues p = pvs[row * n + col];
					if (p != null) {
						p.and(boxPossible);
					}
				}
			}
		}

		return pvs;
	}
	
	public int numberOfFields() {
		return n*n;
	}
	
	public int size() {
		return n;
	}
	
	public int k() {
		return k;
	}
	
	public int get(int i) {
		return grid[i];
	}
	
	public Pair<Set<Integer>, Set<Integer>> set(int i, int val) {
		grid[i] = val;
		return null;
	}
	
	public int get(int row, int col) {
		return this.grid[n*row + col];
	}

	public Pair<Set<Integer>, Set<Integer>> set(int row, int col, int val) {
		this.grid[n*row + col] = val;
		return null;
	}
	
	public int rowFor(int field) {
		return field / n;
	}
	
	public int colFor(int field) {
		return field % n;
	}
	
	public int boxFor(int field) {
		return (rowFor(field) / k) * k + colFor(field) / k;
	}
	
	public RowIterator iterRow(int r) {
		return new RowIterator(this, r);
	}
	
	public ColIterator iterCol(int c) {
		return new ColIterator(this, c);
	}
	
	public BoxIterator iterBox(int b) {
		return new BoxIterator(this, b);
	}
	
	public boolean isLegal() {
		BitSet found = new BitSet(n);
		
		for (int i = 0; i < n; i++) {
			for (int f : iterRow(i)) {
				if (f == 0) continue;
				
				if (found.get(f - 1)) {
					return false;
				}
				
				found.set(f - 1);
			}
			
			found.clear();
			
			for (int f : iterCol(i)) {
				if (f == 0) continue;
				
				if (found.get(f - 1)) {
					return false;
				}
				
				found.set(f - 1);
			}
			
			found.clear();
			
			for (int f : iterBox(i)) {
				if (f == 0) continue;
				
				if (found.get(f - 1)) {
					return false;
				}
				
				found.set(f - 1);
			}
			
			found.clear();
		}
		
		return true;
	}
	
	public boolean isSolved() {
		for (int i = 0; i < grid.length; i++) {
			if (grid[i] == 0) {
				return false;
			}
		}
		
		BitSet found = new BitSet(n);
		
		for (int row = 0; row < n; row++) {
			found.clear();
			
			for (int f : iterRow(row)) {
				found.set(f - 1);
			}
			
			if (found.cardinality() < n) {
				return false;
			}
		}
		
		for (int col = 0; col < n; col++) {
			found.clear();
			
			for (int f : iterCol(col)) {
				found.set(f - 1);
			}

			if (found.cardinality() < n) {
				return false;
			}
		}
		
		for (int box = 0; box < n; box++) {
			found.clear();
			
			for (int f : iterBox(box)) {
				found.set(f - 1);
			}

			if (found.cardinality() < n) {
				return false;
			}
		}
		
		return true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int row = 0; row < size(); row++) {
			for (int col = 0; col < size(); col++) {
				int x = get(row, col);
				
				if (x == 0) {
					sb.append("  ");
				} else if (x < 10) {
					sb.append(x + " ");
				} else {
					sb.append((char)('A' + (x - 10)) + " ");
				}
				
				if (col+1 < size() && (col+1) % k == 0) {
					sb.append("| ");
				}
			}
			
			sb.append(System.lineSeparator());
			

			if (row+1 < size() && (row+1) % k == 0) {
				for (int col = 0; col < size()+k-1; col++) {
					sb.append("- ");
				}
				
				sb.append(System.lineSeparator());
			}
		}
		
		return sb.toString();
	}
	
}
