package model.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import model.Parser;
import model.util.Pair;

public class ParserTest {
	
	// TODO: test exceptions for wrong input

	@Test
	public void parserk2() throws IOException {
		String input =
				"2\n" +
				"1;2;3;4" + "\n" +
				"2;3;4;1" + "\n" +
				"3;4;1;2" + "\n" +
				"4;1;2;3" + "\n" ;
		
		new Parser();
		
		Pair<int[], Integer> parsed = Parser.parseGrid(new StringReader(input));
		
		int[] expected =
				{1,2, 3,4,
				 2,3, 4,1,
				 
				 3,4, 1,2,
				 4,1, 2,3};
		
		assertArrayEquals(parsed.fst, expected);
		assertEquals(parsed.snd.intValue(), 2);
	}
	
	@Test
	public void parserk3() throws IOException {
		String input =
				"3\n" +
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;9;5;.;2;." + "\n" +
				"4;.;.;.;.;.;1;.;." + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;4;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		
		Pair<int[], Integer> parsed = Parser.parseGrid(new StringReader(input));
		
		int[] expected =
				{0,1,0, 3,0,0, 8,0,0,
				 5,0,9, 6,0,0, 7,0,0,
				 7,0,4, 0,9,5, 0,2,0,
				 
				 4,0,0, 0,0,0, 1,0,0,
				 0,2,8, 0,7,1, 0,6,3,
				 0,0,0, 2,0,4, 9,5,0,
				 
				 6,0,3, 0,0,9, 0,0,7,
				 0,0,0, 4,2,0, 5,1,6,
				 0,5,2, 0,8,0, 0,4,0};
		
		assertArrayEquals(parsed.fst, expected);
		assertEquals(parsed.snd.intValue(), 3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void errorFirstLine() throws IOException {
		String input =
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;9;5;.;2;." + "\n" +
				"4;.;.;.;.;.;1;.;." + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;4;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		
		Parser.parseGrid(new StringReader(input));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void errorMissingColumns() throws IOException {
		String input =
				"3\n" +
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;9;5;.;2;." + "\n" +
				"4;.;.;.;.;.;" + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;4;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		
		Parser.parseGrid(new StringReader(input));
	}

	@Test(expected = IllegalArgumentException.class)
	public void errorIllegalCharacter() throws IOException {
		String input =
				"3\n" +
				".;1;.;3;.;.;8;.;." + "\n" +
				"5;.;9;6;.;.;7;.;." + "\n" +
				"7;.;4;.;M;5;.;2;." + "\n" +
				"4;.;.;.;.;.;1;.;." + "\n" +
				".;2;8;.;7;1;.;6;3" + "\n" +
				".;.;.;2;.;D;9;5;." + "\n" +
				"6;.;3;.;.;9;.;.;7" + "\n" +
				".;.;.;4;2;.;5;1;6" + "\n" +
				".;5;2;.;8;.;.;4;." ;
		
		Parser.parseGrid(new StringReader(input));
	}
	
}
