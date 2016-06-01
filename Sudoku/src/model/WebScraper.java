package model;
import java.util.List;

import com.jaunt.Element;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

public class WebScraper {

	// Site only supports k = 3;
	private static final int K = 3;

	public static String getSudokuFromWeb(int level) throws ResponseException,
			NotFound {
		String url = "http://view.websudoku.com/?level=" + level;

		UserAgent ua = new UserAgent();
		ua.visit(url);

		String output = K + "\n";
		
		Element puzzle = ua.doc.findFirst("<table id=puzzle_grid>");
		List<Element> rows = puzzle.getChildElements();

		for (Element row : rows) {
			List<Element> cols = row.getChildElements();
			for (Element col : cols) {
				Element input = col.findFirst("<input>");
				String val = ".";
				
				if (input.hasAttribute("value"))
					val = input.getAt("value");
				
				output += val+";";
			}
			output = output.substring(0, output.length()) + "\n";
		}
		return output;
	}

}
