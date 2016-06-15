package model.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.tactics.NakedPairsTactic;
import model.tactics.UnsolvableException;
import model.util.IntPriorityQueue;

public class NakedPairsTest {

	@Test
	public void nakedPairsColTest() throws IOException, UnsolvableException {
		String input =
				"2\n" +
				"1;.;.;.\n" +
				"2;.;.;.\n" +
				".;.;.;.\n" +
				".;.;.;.\n";
		
		Grid g = new Grid(input);
		
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());

		PossibleValues[] pvs = g.findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		
		PossibleValuesGrid pGrid = new PossibleValuesGrid(g, pvs, pq);
		
		NakedPairsTactic tac = new NakedPairsTactic(g, pGrid);
		
		assertTrue(pvs[9].get(3));
		assertTrue(pvs[9].get(4));
		
		assertTrue(pvs[13].get(3));
		assertTrue(pvs[13].get(4));
		
		tac.apply();
		
		// 9 and 13, now cannot contain 3 or 4 because the two fields above in the box with the given 1, 2 have to contain 3 and 4
			
		assertFalse(pvs[9].get(3));
		assertFalse(pvs[9].get(4));
		
		assertFalse(pvs[13].get(3));
		assertFalse(pvs[13].get(4));
	}
	
	
	@Test
	public void nakedPairsRowTest() throws IOException, UnsolvableException {
		String input =
				"2\n" +
				"1;2;.;.\n" +
				".;.;.;.\n" +
				".;.;.;.\n" +
				".;.;.;.\n";
		
		Grid g = new Grid(input);
		
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());

		PossibleValues[] pvs = g.findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		
		PossibleValuesGrid pGrid = new PossibleValuesGrid(g, pvs, pq);
		
		NakedPairsTactic tac = new NakedPairsTactic(g, pGrid);
		
		assertTrue(pvs[6].get(3));
		assertTrue(pvs[6].get(4));
		
		assertTrue(pvs[7].get(3));
		assertTrue(pvs[7].get(4));
		
		tac.apply();
		
		// 6 and 7, now cannot contain 3 or 4 because the two fields to the left in the box with the given 1, 2 have to contain 3 and 4
			
		assertFalse(pvs[6].get(3));
		assertFalse(pvs[6].get(4));
		
		assertFalse(pvs[7].get(3));
		assertFalse(pvs[7].get(4));
	}
	
	@Test
	public void nakedPairsBoxTest() throws IOException, UnsolvableException {
		String input =
				"2\n" +
				"1;.;.;.\n" +
				".;.;.;.\n" +
				".;2;.;.\n" +
				".;.;.;.\n";
		
		Grid g = new Grid(input);
		
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());

		PossibleValues[] pvs = g.findPossibleValues();

		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		
		PossibleValuesGrid pGrid = new PossibleValuesGrid(g, pvs, pq);
		
		NakedPairsTactic tac = new NakedPairsTactic(g, pGrid);
		
		assertTrue(pvs[4].get(3));
		assertTrue(pvs[4].get(4));
		
		tac.apply();
		
		// 4 cannot contain 3 or 4, because those have to go in 1 and 5
			
		assertFalse(pvs[4].get(3));
		assertFalse(pvs[4].get(4));
	}
	
}
