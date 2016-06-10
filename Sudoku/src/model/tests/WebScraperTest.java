package model.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import model.Grid;
import model.Parser;
import model.WebScraper;

public class WebScraperTest {

//	@Test
	public void webscraperk3() throws NotFound, ResponseException, IOException {

		String input = WebScraper.getSudokuFromWeb(1);
		Grid g = null;
		try {
			g = new Grid(new StringReader(input));
		} catch (Exception e) {
			fail("Input from web not correct");
		}
		assertNotNull(g);
		assertFalse(g.isSolved());
	}
	
//	@Test
	public void getSudoku() throws NotFound, ResponseException, IOException {
		String input = WebScraper.getSudokuFromWeb(1);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("puzzles/evil4.txt"), "utf-8"))) {
		   writer.write(input);
		}
	}
}
