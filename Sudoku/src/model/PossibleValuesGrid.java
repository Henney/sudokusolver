package model;

import java.util.ArrayDeque;
import java.util.Stack;

import model.util.IntLinkedList;
import model.util.IntPriorityQueue;
import model.util.Pair;

public class PossibleValuesGrid {
	
	private Grid grid;
	private IntPriorityQueue pq;
	private PossibleValues[] pvs;
	private ArrayDeque<Pair<Integer, Integer>> changed;
	
	private int countChanged = 0;
	private Stack<Integer> changedHistory;

	public PossibleValuesGrid(Grid grid, PossibleValues[] pvs, IntPriorityQueue pq) {
		this.grid = grid;
		this.pvs = pvs;
		this.pq = pq;
		this.changed = new ArrayDeque<Pair<Integer, Integer>>();
		
		this.changedHistory = new Stack<Integer>();
	}
	
	public void newTransaction() {
		if (countChanged != 0) {
			throw new IllegalStateException("A transaction is already in progress.");
		}
	}
	
	public void endTransaction() {
		changedHistory.push(countChanged);
		countChanged = 0;
	}
	
	public void cancelTransaction() {
		revertCount(countChanged);
		countChanged = 0;
	}
	
	public void revert() {
		if (changedHistory.isEmpty()) {
			throw new IllegalStateException("No transactions have been made.");
		}
		
		int count = changedHistory.pop();
		revertCount(count);
	}
	
	private void revertCount(int count) {
		while (count-- > 0) {
			Pair<Integer, Integer> p = changed.pop();
			int i = p.fst;
			int v = p.snd;
			pvs[i].set(v, true);
			pq.changePrio(i, pvs[i].possible());
		}
	}
	
	public boolean updateField(int i, int x) {
		if (pvs[i] != null && pvs[i].set(x, false)) {
			countChanged++;
			changed.push(new Pair<Integer, Integer>(i, x));
			pq.changePrio(i, pvs[i].possible());
			return true;
		}
		
		return false;
	}
	
	public void setRowImpossible(int field, int value) {
		final int row = grid.rowFor(field);
		final int n = grid.size();
		final int startField = row * n;
		
		for (int c = 0; c < n; c++) {
			updateField(startField + c, value);
		}
	}
	
	public void setColImpossible(int field, int value) {
		final int col = grid.colFor(field);
		final int n = grid.size();
			
		for (int r = 0; r < n; r++) {
			updateField(r * n + col, value);
		}
	}
	
	public void setBoxImpossible(int field, int value) {
		final int row = grid.rowFor(field);
		final int col = grid.colFor(field);
		final int k = grid.k();
		final int n = grid.size();

		final int boxRow = (row / k) * k;
		final int boxCol = (col / k) * k;
		
		for (int r = boxRow; r < boxRow + k; r++) {
			int rn = r * n;

			for (int c = boxCol; c < boxCol + k; c++) {
				updateField(rn+c, value);
			}
		}
	}
	
	public void setConnectedImpossible(int field, int value) {
		setRowImpossible(field, value);
		setColImpossible(field, value);
		setBoxImpossible(field, value);
	}
	
	public IntLinkedList valuesWithPrio(int prio) {
		return pq.valuesWithPrio(prio);
	}
	
	public PossibleValues[] getPossibleValues() {
		return pvs;
	}
	
	public void setOnlyPossible(int field, int value) {
		for (int i = 1; i < value; i++) {
			updateField(field, i);
		}
		
		for (int i = value+1; i <= grid.size(); i++) {
			updateField(field, i);
		}
	}
	
	public int changesMadeInTransaction() {
		return countChanged;
	}
}
