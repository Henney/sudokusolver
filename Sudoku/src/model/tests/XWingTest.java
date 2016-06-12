package model.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.Grid;
import model.PossibleValues;
import model.PossibleValuesGrid;
import model.tactics.ChoiceTactic;
import model.tactics.UnsolvableException;
import model.tactics.XWingTactic;
import model.util.IntPriorityQueue;

public class XWingTest {

	@Test
	public void removeFromCols() throws IOException {
		String input =
				"3\n" +
				"1;.;.;.;.;.;5;6;9;\n" +
				"4;9;2;.;5;6;1;.;8;\n" +
				".;5;6;1;.;9;2;4;.;\n" +
				".;.;9;6;4;.;8;.;1;\n" +
				".;6;4;.;1;.;.;.;.;\n" +
				"2;1;8;.;3;5;6;.;4;\n" +
				".;4;.;.;.;.;.;1;6;\n" +
				"9;.;.;.;6;1;4;.;2;\n" +
				"6;2;1;.;.;.;.;.;5;";

		
		Grid g = new Grid(input);
		assertTrue(g.isLegal());
		// From http://www.sudokuwiki.org/x_wing_strategy
		ArrayList<Integer> fields = new ArrayList<Integer>();
		fields.add(12);
		fields.add(16);
		fields.add(48);
		fields.add(52);
		PossibleValues[] pvs = g.findPossibleValues();
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		PossibleValuesGrid pg = new PossibleValuesGrid(g, pvs, pq);
		
		int val = 7;
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
			int sr = g.rowFor(field)*g.size();
			for (int i = sr; i < sr+g.size(); i++) {
				if (!fields.contains(i)) {
					assertFalse(pvs[i] != null && pvs[i].get(val));
				}
			}

			assertTrue(pvs[field].get(val));
			int x = getPosFromCol(g, pvs, fields, field, val);
			assertTrue(x > 0);
		}
		
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
		}
		
		XWingTactic xw = new XWingTactic(g, pg);
		try {
			xw.apply();
		} catch (UnsolvableException e) {
			fail("Unsolvable");
		}
		
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
			int x = getPosFromCol(g, pvs, fields, field, val);
			assertTrue(x == 0);
		}
	}

	private int getPosFromCol(Grid g, PossibleValues[] pvs, List<Integer> fields, int field, int val) {
		int sc = g.colFor(field);
		int x = 0;
		for (int i = sc; i < g.numberOfFields(); i += g.size()) {
			if (!fields.contains(i) && pvs[i] != null && pvs[i].get(val)){
				x++;
			}
		}
		return x;
	}
	
	@Test
	public void removeFromRows() throws IOException {
		String input =
				"3\n" +
				"1;4;.;.;.;2;.;9;6;\n" +
				".;9;5;.;6;1;4;.;2;\n" +
				".;2;6;9;4;8;.;.;1;\n" +
				".;.;1;6;.;.;.;.;.;\n" +
				".;5;.;4;1;3;.;6;.;\n" +
				".;6;9;.;.;5;.;1;.;\n" +
				"5;1;2;8;.;6;.;4;.;\n" +
				"6;.;4;.;.;.;1;.;.;\n" +
				"9;8;.;1;.;4;6;2;5;";
		
		Grid g = new Grid(input);
		assertTrue(g.isLegal());
		// From http://www.sudokuwiki.org/x_wing_strategy - transposed
		ArrayList<Integer> fields = new ArrayList<Integer>();
		fields.add(28);
		fields.add(64);
		fields.add(32);
		fields.add(68);
		PossibleValues[] pvs = g.findPossibleValues();
		IntPriorityQueue pq = new IntPriorityQueue(g.numberOfFields(), g.size());
		for (int i = 0; i < pvs.length; i++) {
			if (pvs[i] != null) {
				pq.insert(i, pvs[i].possible());
			}
		}
		PossibleValuesGrid pg = new PossibleValuesGrid(g, pvs, pq);
		
		int val = 7;
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
			int sc = g.colFor(field);
			for (int i = sc; i < g.numberOfFields(); i += g.size()) {
				if (!fields.contains(i)) {
					assertFalse(pvs[i] != null && pvs[i].get(val));
				}
			}

			assertTrue(pvs[field].get(val));
			int x = getPosFromRow(g, pvs, fields, field, val);
			assertTrue(x > 0);
		}
		
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
		}
		
		XWingTactic xw = new XWingTactic(g, pg);
		
		try {
			xw.apply();
		} catch (UnsolvableException e) {
			fail("Unsolvable");
		}
		
		for (int field : fields) {
			assertTrue(pvs[field].get(val));
			int x = getPosFromRow(g, pvs, fields, field, val);
			assertTrue(x == 0);
		}
	}

	private int getPosFromRow(Grid g, PossibleValues[] pvs, List<Integer> fields, int field, int val) {
		int sr = g.rowFor(field)*g.size();
		int x = 0;
		for (int i = sr; i < sr+g.size(); i++) {
			if (!fields.contains(i) && pvs[i] != null && pvs[i].get(val)){
				x++;
			}
		}
		return x;
	}
}
