package model.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.tactics.UniqueCandidateTactic;
import model.tactics.UnsolvableException;
import model.util.IntPriorityQueue;

public class UniqueCandidateTest {

	@Test
	public void removePos() throws IOException {
		String input =
				"3\n" +
				".;2;3;.;.;.;.;.;.;\n" +
				".;.;.;1;.;.;.;.;.;\n" +
				".;.;.;.;.;.;1;.;.;\n" +
				".;.;.;.;.;.;.;.;.;\n" +
				".;.;.;.;.;.;.;.;.;\n" +
				".;.;.;.;.;.;.;.;.;\n" +
				".;.;.;.;.;.;.;.;.;\n" +
				".;.;.;.;.;.;.;.;.;\n" +
				".;.;.;.;.;.;.;.;.;";

		
		Grid g = new Grid(input);
		assertTrue(g.isLegal());
		PossibleValues[] pvs = g.findPossibleValues();
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		PossibleValuesGrid pg = new PossibleValuesGrid(g, pvs, pq);
		
		assertTrue(pvs[0].possible() > 1);
		
		UniqueCandidateTactic uc = new UniqueCandidateTactic(g, pg);
		try {
			uc.apply();
		} catch (UnsolvableException e) {
			fail("Unsolvable");
		}

		assertTrue(pvs[0].possible() == 1);
		assertTrue(pvs[0].get(1));
		
	}
}
