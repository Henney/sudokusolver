package model.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;

import model.Parser;
import model.WebScraper;

public class WebScraperTest {

	@Test
	public void webscraperk3() throws NotFound, ResponseException, IOException {

		String input = new WebScraper().getSudokuFromWeb(1);
		try {
			Parser.parseGrid(new StringReader(input));
			// TODO Confirm no error in Parser;
		} catch (Exception e) {
			fail("Input from web not correct");
		}
	}
}
