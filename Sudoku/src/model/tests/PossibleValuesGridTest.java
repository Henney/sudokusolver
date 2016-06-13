package model.tests;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.util.IntPriorityQueue;

public class PossibleValuesGridTest {

	@Test(expected=IllegalStateException.class)
	public void invalidTransaction() {
		PossibleValuesGrid pGrid = initPGrid();
		pGrid.newTransaction();
		pGrid.setImpossible(0, 1);
		pGrid.newTransaction();
	}
	
	@Test(expected=IllegalStateException.class)
	public void revertNoTransaction() {
		PossibleValuesGrid pGrid = initPGrid();
		pGrid.revert();
	}
	
	private PossibleValuesGrid initPGrid() {
		Grid g = new Grid(3);		
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		PossibleValues[] pvs = g.findPossibleValues();
		for (int i = 0; i < pvs.length; i++) {
			pq.insert(i, pvs[i].possible());
		}
		return new PossibleValuesGrid(g, pvs, pq);
		
	}
	
}
