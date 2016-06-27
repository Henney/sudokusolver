package model;

//import com.jaunt.NotFound;
//import com.jaunt.ResponseException;
//import com.jaunt.UserAgent;

public class WebScraper {

	// Site only supports k = 3;
	private static final int K = 3;
	private static final int N = K*K;
	private static final int FIELDS = N*N;
	
	private final static char DELIMITER = ';';
	private final static char ZERO = '.';

	public static String getSudokuFromWeb(int level) /* throws ResponseException,
			NotFound */ {
		/*
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
		*/
		
		// Add above and Jaunt to support downloading from the internet.
		return null;
	}

}
