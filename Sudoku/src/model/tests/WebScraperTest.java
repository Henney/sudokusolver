package model.tests;

import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import org.junit.Test;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import model.Parser;
import model.WebScraper;

public class WebScraperTest {

//	@Test
	public void webscraperk3() throws NotFound, ResponseException, IOException {

		String input = new WebScraper().getSudokuFromWeb(1);
		try {
			Parser.parseGrid(new StringReader(input));
			// TODO Confirm no error in Parser;
		} catch (Exception e) {
			fail("Input from web not correct");
		}
	}
	
//	@Test
	public void getSudoku() throws NotFound, ResponseException, IOException {
		String input = new WebScraper().getSudokuFromWeb(1);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("puzzles/evil4.txt"), "utf-8"))) {
		   writer.write(input);
		}
	}
}
