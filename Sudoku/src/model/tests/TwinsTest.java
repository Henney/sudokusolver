package model.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.tactics.TwinsTactic;
import model.tactics.UnsolvableException;
import model.util.IntPriorityQueue;

public class TwinsTest {

	@Test
	public void twinsTest() throws IOException, UnsolvableException {
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
		
		TwinsTactic tac = new TwinsTactic(g, pGrid);
		
		tac.apply();
		
		// 9 and 13, now cannot contain 3 or 4 because those have to be in the same box as the given 1, 2
			
		assertFalse(pvs[9].get(3));
		assertFalse(pvs[9].get(4));
		
		assertFalse(pvs[13].get(3));
		assertFalse(pvs[13].get(4));
		
		assertFalse(pvs[8].get(1));
		assertFalse(pvs[8].get(2));
		
		assertFalse(pvs[12].get(1));
		assertFalse(pvs[12].get(2));
	}
	
}
