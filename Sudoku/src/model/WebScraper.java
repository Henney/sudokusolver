package model;

import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

public class WebScraper {

	// Site only supports k = 3;
	private static final int K = 3;
	private static final int N = K*K;
	private static final int FIELDS = N*N;
	
	private final static char DELIMITER = ';';
	private final static char ZERO = '.';

	public static String getSudokuFromWeb(int level) throws ResponseException,
			NotFound {
		String url = "http://www.sudokukingdom.com/";
		UserAgent ua = new UserAgent();
		ua.visit(url);
		url = "http://www.sudokukingdom.com/games/sudoku/puzzle/newpuzzle.php";
		ua.sendPOST(url, "level=" + level);

		String data = ua.doc.innerHTML();
		String sudoku = data.substring(data.lastIndexOf('@', data.length()-FIELDS)+1);
		String output = K+"";
		for (int i = 0; i < FIELDS; i++) {
			if (i % N == 0) {
				output += "\n";
			}
			char c = sudoku.charAt(i) == '0' ? ZERO : sudoku.charAt(i);
			output += c;
			output += DELIMITER;
		}
		return output;
	}
	
//	public static String getSudokuFromWebOld(int level) throws ResponseException, NotFound {
//		
//		UserAgent ua = new UserAgent();
//		String output = K + "\n";
//		String url = "http://view.websudoku.com/?level=" + level;
//		ua.visit(url);
//		Element puzzle = ua.doc.findFirst("<table id=puzzle_grid>");
//		List<Element> rows = puzzle.getChildElements();
//
//		for (Element row : rows) {
//			List<Element> cols = row.getChildElements();
//			for (Element col : cols) {
//				Element input = col.findFirst("<input>");
//				String val = ".";
//				
//				if (input.hasAttribute("value"))
//					val = input.getAt("value");
//				
//				output += val+";";
//			}
//			output = output.substring(0, output.length()) + "\n";
//		}
//		return output;
//	}

}
