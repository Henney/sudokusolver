package model.tests;

import java.io.IOException;

import org.junit.Test;

public class GivenSudokusTest {

	@Test
	public void runGivenTests() throws IOException {
		String path = "puzzles/given/Puzzle_";
		String ext = ".dat";
		String[] nums = { "3_01", "3_02", "3_03", "3_04", "3_05", "3_06", "3_07", 
				"3_08", "3_09", "3_10", "4_01", "4_02", "5_01", "6_01"  };
		for (String num : nums) {
			TacticSolverTest.solveSudoku(path + num + ext);
		}
		
		String unsol = "3_X1";
		TacticSolverTest.solveImpossible(path + unsol + ext);
	}
}
