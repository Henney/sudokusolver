package model.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import model.Grid;
import model.TacticSolver;
import model.WebScraper;

public class WebScraperTest {

	@Test
	public void webscraperk3() throws NotFound, ResponseException, IOException {

		String input = WebScraper.getSudokuFromWeb(4);
		Grid g = null;
		try {
			g = new Grid(new StringReader(input));
		} catch (Exception e) {
			fail("Input from web not correct");
		}
		assertNotNull(g);
		assertFalse(g.isSolved());
		TacticSolver s = new TacticSolver(g);
		assertTrue(s.solvable());
		assertTrue(s.unique());
	}
}
